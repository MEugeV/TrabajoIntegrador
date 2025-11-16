/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Models.ActividadPrincipal;
import Models.DomicilioFiscal;
import Models.Empresa;
import Models.Labelled;
import Models.Pais;
import Models.Provincia;
import Service.DomicilioFiscalServiceImpl;
import Service.EmpresaServiceImpl;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author eugeniavogt
 */
public class MenuHandler {

    private final Scanner scanner;
    private final EmpresaServiceImpl empresaService;
    private final DomicilioFiscalServiceImpl domicilioFiscalService;

    public MenuHandler(Scanner scanner, EmpresaServiceImpl empresaService, DomicilioFiscalServiceImpl domicilioFiscalService) {
        this.scanner = scanner;
        this.empresaService = empresaService;
        this.domicilioFiscalService = domicilioFiscalService;
    }

    /**
     * Recibe array de elementos que implementen la interfaz labelled
     * Renderiza los labels de Enums como opciones seleccionables
     */
    public <T extends Labelled> void showEnum(T[] enumArray) {
        for (int i = 0; i < enumArray.length; i++) {
            System.out.println((i + 1) + ". " + enumArray[i].getLabel());
        }
    }

    /**
     * Valida y maneja errores del ingreso de inputs numéricos en rangos predefinidos
     */
    public Integer parseNumber(Scanner sc, int min, int max, Boolean allowEmpty) {
        while (true) {
            String line = scanner.nextLine().trim();
            if (allowEmpty && line.isEmpty()) {
                return null;
            }
            try {
                int number = Integer.parseInt(line);
                if (number < min || number > max) {
                    System.out.print("Número fuera de rango, intente nuevamente: ");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    public int parseNumber(Scanner sc, int min, int max) {
        return parseNumber(sc, min, max, false);
    }

    /**
     * En transacción con la creación de domicilio fiscal
     */
    public void crearEmpresa() {
        try {
            System.out.println("Ingrese los datos de su empresa: ");
            System.out.println("Razón Social: ");
            String razonSocial = scanner.nextLine();
            System.out.println("Cuit (sin guiónes): ");
            String cuit = scanner.nextLine();
            ActividadPrincipal[] actividades = ActividadPrincipal.values();
            System.out.println("Actividades Principales: ");
            showEnum(actividades);
            System.out.print("Ingrese el número correspondiente a su actividad principal: ");
            int opcion = parseNumber(scanner, 1, actividades.length);

            String actividadPrincipal = actividades[opcion - 1].getLabel();
            System.out.println("Email: ");
            String email = scanner.nextLine();

            Empresa empresa = new Empresa(0, razonSocial, cuit, ActividadPrincipal.fromLabel(actividadPrincipal), email);

            System.out.println("¿Creará un domicilio fiscal para su empresa?:  \n 1. Si \n 2. No");
            int seleccion = parseNumber(scanner, 1, 2);
            if (seleccion == 2) {
                empresaService.insertar(empresa);
            } else {
                DomicilioFiscal domicilio = crearDomicilio();
                empresaService.insertarConDomicilio(empresa, domicilio);
            }

        } catch (Exception e) {
            System.out.println("Error al insertar empresa: " + e.getMessage());

        }

    }

    public DomicilioFiscal crearDomicilio() {
        try {
            System.out.println("Ingrese los datos de su domicilio fiscal: ");
            System.out.println("Calle: ");
            String calle = scanner.nextLine();
            System.out.println("Número: ");
            int numero = parseNumber(scanner, 1, Integer.MAX_VALUE);
            System.out.println("Ciudad: ");
            String ciudad = scanner.nextLine();
            Provincia[] provincias = Provincia.values();
            System.out.println("Provincia: ");
            showEnum(provincias);
            System.out.print("Ingrese el número correspondiente a su provincia: ");
            int opcion = parseNumber(scanner, 1, provincias.length);

            String provincia = provincias[opcion - 1].getLabel();
            System.out.println("Código postal: ");
            String codigoPostal = scanner.nextLine();
            Pais[] paises = Pais.values();
            System.out.println("País: ");
            showEnum(paises);
            System.out.print("Ingrese el número correspondiente a su país: ");
            int selection = parseNumber(scanner, 1, paises.length);

            String pais = paises[selection - 1].getLabel();
            DomicilioFiscal domicilio = new DomicilioFiscal(0, calle, numero, ciudad, Provincia.fromLabel(provincia), codigoPostal, Pais.fromLabel(pais));
            return domicilio;

        } catch (Exception e) {
            System.out.println("Error al crear domicilio fiscal: " + e.getMessage());
        }
        return null;

    }
    
    /**
     * En transacción con la actualización de domicilio fiscal
     */
    public void actualizarEmpresa() {
        try {
            System.out.println("CUIT de la empresa a actualizar (sin guiónes): ");
            String cuit = scanner.nextLine();
            Empresa e = empresaService.getByCuit(cuit);

            if (e != null) {
                System.out.println("Ingrese sólo el valor de los campos a modificar: ");
                System.out.println("Nueva Razón Social (" + e.getRazonSocial() + "): ");
                String razonSocial = scanner.nextLine();
                if (!razonSocial.isBlank()) {
                    e.setRazonSocial(razonSocial);
                }
                System.out.println("Nuevo Cuit (sin guiónes) (" + e.getCuit() + "): ");
                String newCuit = scanner.nextLine();
                if (!newCuit.isBlank()) {
                    e.setCuit(newCuit);
                }
                ActividadPrincipal[] actividades = ActividadPrincipal.values();
                System.out.println("Actividades Principales: ");
                showEnum(actividades);
                System.out.print("Número correspondiente a su nueva actividad principal (" + e.getActividadPrincipal().getLabel() + "): ");
                Integer opcion = parseNumber(scanner, 1, actividades.length, true);
                if (opcion != null) {
                    e.setActividadPrincipal(actividades[opcion - 1]);
                }
                System.out.println("Nuevo Email: (" + e.getEmail() + "): ");
                String email = scanner.nextLine();
                if (!email.isBlank()) {
                    e.setEmail(email);
                }
                DomicilioFiscal d;
                if (e.getDomicilioFiscal() != null) {
                    d = domicilioFiscalService.getById(e.getDomicilioFiscal().getId());
                    actualizarDomicilio(d);
                    empresaService.actualizarConDomicilio(e, d);

                } else {
                    empresaService.actualizar(e);
                }
            } else {
                System.out.println("Empresa no encontrada");
            }

        } catch (Exception e) {
            System.out.println("Error actualizar empresa: " + e.getMessage());
        }

    }

    public void actualizarDomicilio(DomicilioFiscal d) {
        try {
            System.out.println("Ingrese sólo el valor de los campos a modificar: ");
            System.out.println("Nueva calle (" + d.getCalle() + "): ");
            String calle = scanner.nextLine();
            if (!calle.isBlank()) {
                d.setCalle(calle);
            }
            System.out.println("Nuevo número (" + d.getNumero() + "): ");
            Integer numero = parseNumber(scanner, 1, Integer.MAX_VALUE, true);
            if (numero != null) {
                d.setNumero(numero);
            }
            System.out.println("Nueva ciudad (" + d.getCiudad() + "): ");
            String ciudad = scanner.nextLine();
            if (!ciudad.isBlank()) {
                d.setCiudad(ciudad);
            }
            System.out.println("Nuevo código postal (" + d.getCodigoPostal() + "): ");
            String codigoPostal = scanner.nextLine();
            if (!codigoPostal.isBlank()) {
                d.setCodigoPostal(codigoPostal);
            }
            Provincia[] provincias = Provincia.values();
            System.out.println("Provincias: ");
            showEnum(provincias);
            System.out.print("Número correspondiente a su nueva provincia (" + d.getProvincia().getLabel() + "): ");
            Integer opcion = parseNumber(scanner, 1, provincias.length, true);
            if (opcion != null) {
                d.setProvincia(provincias[opcion - 1]);
            }

        } catch (Exception e) {
            System.out.println("Error actualizar domicilio fiscal: " + e.getMessage());
        }

    }

    /**
     * En transacción con la eliminación de domicilio fiscal
     */
    public void eliminarEmpresa() {
        try {
            System.out.println("CUIT de la empresa a eliminar (sin guiónes): ");
            String cuit = scanner.nextLine();
            Empresa empresa = empresaService.getByCuit(cuit);

            if (empresa != null) {
                empresaService.eliminarConDomicilio(empresa.getId());
            } else {
                System.out.println("Empresa no encontrada");
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar empresa: " + e.getMessage());
        }
    }

    /**
    * En transacción con la recuperación de domicilio fiscal
    */
    public void recuperarEmpresa() {
        {
            try {
                System.out.println("CUIT de la empresa a recuperar (sin guiónes): ");
                String cuit = scanner.nextLine();
                Empresa empresa = empresaService.getByCuit(cuit, true);

                if (empresa != null) {
                    empresaService.recuperarConDomicilio(empresa.getId());
                } else {
                    System.out.println("Empresa no encontrada");
                }

            } catch (Exception e) {
                System.out.println("Error al recuperar empresa: " + e.getMessage());
            }

        }
    }

    public void listarDomicilios() {
        try {
            List<DomicilioFiscal> domicilios = domicilioFiscalService.getAll();

            if (domicilios == null || domicilios.isEmpty()) {
                System.out.println("No hay domicilios fiscales en la selección");
            } else {
                System.out.println("Listado de domicilios fiscales activos: ");
                for (DomicilioFiscal df : domicilios) {
                    System.out.println("Domicilio fiscal: " + df.toString());
                }
            }

        } catch (Exception e) {
            System.out.println("Error al listar domicilios fiscales: " + e.getMessage());
        }
    }

    public void listarEmpresas() {
        try {
            System.out.println("¿Desea \n 1. Listar todas las empresas \n 2. Buscar empresa por razón social \n 3. Buscar empresa por CUIT \n 4. Buscar empresa por ID ");
            int subopcion = parseNumber(scanner, 1, 4);
            if (subopcion == 3) {
                System.out.println("Ingrese el Cuit a buscar (sin guiónes): ");
                String cuit = scanner.nextLine();
                Empresa e = empresaService.getByCuit(cuit);
                if (e == null) {
                    System.out.println("No se encontró una empresa coincidente con la selección");
                } else {
                    System.out.println("Empresa: " + e.toString());
                }
            } else if (subopcion == 4) {
                System.out.println("Ingrese el ID de empresa a buscar: ");
                int id = parseNumber(scanner, 1, Integer.MAX_VALUE);
                Empresa e = empresaService.getById(id);
                if (e == null) {
                    System.out.println("No se encontró una empresa coincidente con la selección");
                } else {
                    System.out.println("Empresa: " + e.toString());
                }

            } else {
                List<Empresa> empresas;
                if (subopcion == 1) {
                    empresas = empresaService.getAll();
                } else {
                    System.out.println("Ingrese la razón social a buscar: ");
                    String razonSocial = scanner.nextLine();
                    empresas = empresaService.getByRazonSocial(razonSocial);
                }
                if (empresas == null || empresas.isEmpty()) {
                    System.out.println("No hay empresas en la selección");
                } else {
                    System.out.println("Listado de empresas activas: ");
                    for (Empresa e : empresas) {
                        System.out.println("Empresa: " + e.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error listar empresas: " + e.getMessage());
        }
    }

}
