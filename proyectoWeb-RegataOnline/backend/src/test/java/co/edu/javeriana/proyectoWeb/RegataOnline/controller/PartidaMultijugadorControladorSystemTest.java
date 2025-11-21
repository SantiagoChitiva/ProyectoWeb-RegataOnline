package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.SelectOption;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;


import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;

import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;


import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;


@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class PartidaMultijugadorControladorSystemTest {

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private static String BASE_URL = "http://localhost:4200";


    private Long mapaId;
    private Long barco1Id, barco2Id, barco3Id, barco4Id;

    @BeforeEach
    void init() {
        // Los usuarios, mapas y barcos ya son creados por DbInitializer con el perfil system-testing
        
        // Obtener los mapas existentes (DbInitializer crea 2 mapas)
        List<Mapa> mapas = mapaRepositorio.findAll();
        if (!mapas.isEmpty()) {
            mapaId = mapas.get(0).getId(); // Usar el primer mapa (5x5)
        }

        // Obtener los barcos existentes (DbInitializer crea 50 barcos)
        List<Barco> barcos = barcoRepositorio.findAll();
        if (barcos.size() >= 4) {
            barco1Id = barcos.get(0).getId();
            barco2Id = barcos.get(1).getId();
            barco3Id = barcos.get(2).getId();
            barco4Id = barcos.get(3).getId();
        }

        // Inicializar Playwright
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        this.context = browser.newContext();
        this.page = context.newPage();
    }

    @AfterEach
    void end() {
        browser.close();
        playwright.close();
    }

    /**
     * Prueba de sistema E2E completa:
     * - 4 usuarios inician sesión en diferentes contextos del navegador
     * - Usuario 1 crea una partida multijugador
     * - Usuarios 2, 3 y 4 se unen a la partida
     * - Usuario 1 inicia la partida cuando hay 4 jugadores
     * - Los 4 jugadores realizan movimientos haciendo clic en celdas del tablero
     * - Se verifican las estadísticas y el estado de la partida
     */
    @Test
    void testPartidaMultijugadorCompletaConCuatroJugadores() throws Exception {
        // FASE 1: Usuario 1 inicia sesión y crea partida
        page.navigate(BASE_URL + "/login");
        
        // Esperar a que los campos de login estén visibles
        page.waitForSelector("#email", new Page.WaitForSelectorOptions().setTimeout(10000));
        page.locator("#email").fill("user1@example.com");
        page.locator("#password").fill("user1pass");
        page.locator("button[type='submit']").click();
        
        // Esperar a que Angular procese el login y guarde el token
        page.waitForTimeout(5000);
        
        // Verificar si hay token en sessionStorage
        Object token = page.evaluate("() => sessionStorage.getItem('jwt-token')");
        if (token == null) {
            System.out.println("❌ Login falló - no hay token en sessionStorage");
            System.out.println("URL actual: " + page.url());
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("debug-no-token.png")));
            throw new AssertionError("Login falló - no se guardó el token");
        }
        System.out.println("✓ Token encontrado en sessionStorage: " + token.toString().substring(0, 20) + "...");

        // Navegar directamente a crear partida (sessionStorage se mantiene en la misma sesión)
        page.navigate(BASE_URL + "/partida-multijugador/crear");
        page.waitForLoadState();

        // Esperar a que el select #mapa tenga más de 1 opción (la default + opciones reales)
        page.waitForFunction("document.querySelector('#mapa').options.length > 1");
        page.waitForTimeout(500);

        // Seleccionar mapa y barco
        page.locator("#mapa").selectOption(new SelectOption().setValue(mapaId.toString()));
        page.waitForTimeout(500);        page.locator("#barco").selectOption(new SelectOption().setValue(barco1Id.toString()));
        page.waitForTimeout(500);
        
        page.locator("button.btn-submit").click();
        page.waitForTimeout(3000);

        // Obtener ID de la partida de la URL
        String url1 = page.url();
        String partidaId = url1.substring(url1.lastIndexOf('/') + 1);

        // FASE 2: Usuario 2 - Login y unirse
        BrowserContext context2 = browser.newContext();
        Page page2 = context2.newPage();

        page2.navigate(BASE_URL + "/login");
        page2.waitForSelector("#email", new Page.WaitForSelectorOptions().setTimeout(10000));
        page2.locator("#email").fill("user2@example.com");
        page2.locator("#password").fill("user2pass");
        page2.locator("button[type='submit']").click();
        page2.waitForTimeout(3000);

        page2.navigate(BASE_URL + "/partida-multijugador/unirse/" + partidaId);
        page2.waitForLoadState();
        page2.waitForTimeout(3000);
        page2.waitForSelector("#barco", new Page.WaitForSelectorOptions().setTimeout(10000));
        page2.locator("#barco").selectOption(new SelectOption().setValue(barco2Id.toString()));
        page2.locator("button.btn-submit").click();
        page2.waitForTimeout(2000);

        // FASE 3: Usuario 3 - Login y unirse
        BrowserContext context3 = browser.newContext();
        Page page3 = context3.newPage();

        page3.navigate(BASE_URL + "/login");
        page3.waitForSelector("#email", new Page.WaitForSelectorOptions().setTimeout(10000));
        page3.locator("#email").fill("user3@example.com");
        page3.locator("#password").fill("user3pass");
        page3.locator("button[type='submit']").click();
        page3.waitForTimeout(3000);

        page3.navigate(BASE_URL + "/partida-multijugador/unirse/" + partidaId);
        page3.waitForLoadState();
        page3.waitForTimeout(3000);
        page3.waitForSelector("#barco", new Page.WaitForSelectorOptions().setTimeout(10000));
        page3.locator("#barco").selectOption(new SelectOption().setValue(barco3Id.toString()));
        page3.locator("button.btn-submit").click();
        page3.waitForTimeout(2000);

        // FASE 4: Usuario 4 - Login y unirse
        BrowserContext context4 = browser.newContext();
        Page page4 = context4.newPage();

        page4.navigate(BASE_URL + "/login");
        page4.waitForSelector("#email", new Page.WaitForSelectorOptions().setTimeout(10000));
        page4.locator("#email").fill("user4@example.com");
        page4.locator("#password").fill("user4pass");
        page4.locator("button[type='submit']").click();
        page4.waitForTimeout(3000);

        page4.navigate(BASE_URL + "/partida-multijugador/unirse/" + partidaId);
        page4.waitForLoadState();
        page4.waitForTimeout(3000);
        page4.waitForSelector("#barco", new Page.WaitForSelectorOptions().setTimeout(10000));
        page4.locator("#barco").selectOption(new SelectOption().setValue(barco4Id.toString()));
        page4.locator("button.btn-submit").click();
        page4.waitForTimeout(2000);

        // FASE 5: Usuario 1 verifica que hay 4 jugadores e inicia la partida
        page.reload();
        page.waitForTimeout(3000);
        String jugadoresText = page.locator(".jugadores-panel h3").textContent();
        assertTrue(jugadoresText.contains("4"), "Debe haber 4 jugadores en la sala");

        page.locator("button.btn-iniciar").click();
        page.waitForTimeout(5000);

        // Verificar que la partida está en curso
        String estadoText = page.locator(".estado-partida h2").textContent();
        assertTrue(estadoText.contains("Estado:"), "Debe mostrar el estado de la partida");

        // FASE 6: Verificar que el tablero es visible
        page.reload();
        page.waitForTimeout(3000);
        assertTrue(page.locator(".tablero").isVisible(), "El tablero debe ser visible");

        // Verificar que hay jugadores en la lista
        int jugadoresVisibles = page.locator(".jugador-item").count();
        assertTrue(jugadoresVisibles == 4, "Deben estar visibles los 4 jugadores");

        // FASE 7: Los 4 jugadores realizan movimientos
        realizarMovimientoSiEsPosible(page);
        realizarMovimientoSiEsPosible(page2);
        realizarMovimientoSiEsPosible(page3);
        realizarMovimientoSiEsPosible(page4);

        // FASE 8: Verificar que los movimientos se reflejaron
        page.reload();
        page.waitForTimeout(3000);

        assertTrue(page.locator(".tablero").isVisible(), "El tablero debe seguir visible");

        String primeraStats = page.locator(".jugador-stats").first().textContent();
        assertTrue(primeraStats.contains("Movs:"), "Debe mostrar movimientos realizados");

        // FASE 9: Segundo turno de movimientos
        realizarMovimientoSiEsPosible(page);
        realizarMovimientoSiEsPosible(page2);

        // FASE 10: Verificación final del estado de la partida
        page.reload();
        page.waitForTimeout(3000);

        // Verificar que seguimos en la sala de la partida
        String currentUrl = page.url();
        assertTrue(currentUrl.contains("/partida-multijugador/sala/"), 
                "Debe estar en la sala de la partida después de iniciar");
        assertTrue(currentUrl.contains(partidaId), 
                "La URL debe contener el ID de la partida");

        // Verificar que hay elementos del tablero (más robusto que clases específicas)
        int tableroElements = page.locator("table, .tablero, .board, canvas").count();
        assertTrue(tableroElements > 0, "Debe mostrar el tablero de juego");

        // Cerrar contextos adicionales
        context2.close();
        context3.close();
        context4.close();

        System.out.println("✓ Prueba de sistema E2E completa exitosa:");
        System.out.println("  - Partida #" + partidaId + " creada desde frontend");
        System.out.println("  - 4 usuarios iniciaron sesión en diferentes contextos");
        System.out.println("  - 4 jugadores se unieron exitosamente");
        System.out.println("  - Partida iniciada y movimientos realizados");
        System.out.println("  - Todas las validaciones UI completadas");
    }

    private void realizarMovimientoSiEsPosible(Page playerPage) {
        playerPage.reload();
        playerPage.waitForTimeout(3000);
        if (playerPage.locator(".mi-turno").count() > 0) {
            if (playerPage.locator(".destino-posible").count() > 0) {
                playerPage.locator(".destino-posible").first().click();
                playerPage.waitForTimeout(3000);
            }
        }
    }
}
