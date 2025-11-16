/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Dao.DomicilioFiscalDAO;
import Dao.EmpresaDAO;
import Service.DomicilioFiscalServiceImpl;
import Service.EmpresaServiceImpl;
import java.util.Scanner;

/**
 *
 * @author eugeniavogt
 */
public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;

    /**
     * Instancias únicas de scanner, services en conexión con DAO y Menu Handler de la aplicación.
     */
    private AppMenu() {
        this.scanner = new Scanner(System.in);
        DomicilioFiscalServiceImpl domicilioFiscalService = new DomicilioFiscalServiceImpl(new DomicilioFiscalDAO());
        EmpresaServiceImpl empresaService = new EmpresaServiceImpl(new EmpresaDAO(), new DomicilioFiscalDAO());
        this.menuHandler = new MenuHandler(scanner, empresaService, domicilioFiscalService);
    }

    /**
     * La aplicación se ejecuta hasta seleccionar la opción del menú de cierre
     * de aplicación
     */
    private void run() {
        try {
            int opcion = -1;
            do {

                menu();
                opcion = menuHandler.parseNumber(scanner, 0, 6);
                processOption(opcion);

            } while (opcion != 0);
            scanner.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void processOption(int opcion) throws Exception {
        switch (opcion) {
            case 1 ->
                menuHandler.crearEmpresa();
            case 2 ->
                menuHandler.listarEmpresas();
            case 3 ->
                menuHandler.actualizarEmpresa();
            case 4 ->
                menuHandler.eliminarEmpresa();
            case 5 ->
                menuHandler.recuperarEmpresa();
            case 6 ->
                menuHandler.listarDomicilios();
        }

    }

    /**
     * Opciones del menú CRUD Operaciones con transacción en entidades
     * relacionadas Empresa y Domicilio Fiscalpara mantener integridad referencial
     */
    private void menu() {
        System.out.println("\n========= MENÚ =========");
        System.out.println("1. Crear empresa");
        System.out.println("2. Listar empresas");
        System.out.println("3. Actualizar empresa");
        System.out.println("4. Eliminar empresa");
        System.out.println("5. Recuperar empresa");
        System.out.println("6. Listar domicilios fiscales");
        System.out.println("0. Salir de la aplicación");
        System.out.println("Ingrese una opción: ");

    }

     /**
     * Instancia la aplicación y inicia su ejecución
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
    
}
