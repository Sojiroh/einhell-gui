package cl.facele.einhell.html;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Transforma {

	public static void toTXT57(DTEBean bean) throws Exception {
		
		Collection<Map<String, String>> detalle = new ArrayList<Map<String, String>> ( );
                Map<String, String> encabeza = new HashMap<String, String>();
		Collection<Map<String, String>> subdescuentos = new ArrayList<Map<String, String>> ( );
		Collection<Map<String, String>> codigos = new ArrayList<Map<String, String>> ( );
		Collection<Map<String, String>> referehncias = new ArrayList<Map<String, String>> ( );
		String txt = "";
		String encabezadoArray[] = null;
		String detalleArray[] = null;
		String vlibresArray[] = null;
		String referenciaArray[] = null;
		String actecoArray[] = null;
		String codigosArray[] = null;
                String guia = null;
                String guia2 = null;
                String datoexterno =null;
                String notaventa = null;
                String fecha = null;
                String dias="";
                String condicionpago = null;

		try {
//			
			String nombreFichero = bean.getPathtxt();
			
			BufferedReader br = null;
                        
                        
              
              String HTML = bean.getContenido().replaceAll("&nbsp;","");
            
              int contador = 0;

                    
                    bean.setFolioDTE(retornaFolio(bean.getContenido().replaceAll("&nbsp;","")));
                    for (int i=1;i<12;i++){
                        
                        
                        switch(i){
                            case 1:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),1);
                                encabeza.put("Fecha Emision", encabezadoArray[0]+"-"+encabezadoArray[1]+"-20"+encabezadoArray[2]);
                                Locale locale1 = new Locale("es", "CL", "WIN");
                                final String OLD_FORMAT = "dd-MMM-yyyy";
                                final String NEW_FORMAT = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, locale1);
                            Date d = sdf.parse(encabeza.get("Fecha Emision"));
                            sdf.applyPattern(NEW_FORMAT);
                            String newDateString = sdf.format(d);
                                encabeza.put("Fecha Emision", newDateString);
                                fecha=newDateString;
                                notaventa=encabezadoArray[3];
                                break;
                            case 2:
                               encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),2);
                                encabeza.put("Razon Social Receptor", encabezadoArray[0]);
                                encabeza.put("Codigo Cliente", encabezadoArray[1]);
                                encabeza.put("Rut Receptor", encabezadoArray[2]);
                                
                                break;
                            case 3:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Direccion Receptor", encabezadoArray[0]);
                                encabeza.put("Comuna Receptor", encabezadoArray[1]);
                                break;
                            case 4:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Telefono", encabezadoArray[2]);
                                guia = encabezadoArray[11];
                                guia2 = encabezadoArray[5];
                                condicionpago = encabezadoArray[17];
                                if(condicionpago.equals("Pago Contado"))
                                    encabeza.put("Forma de pago","1");
                                else{
                                    encabeza.put("Forma de pago","2");
                                    dias="dias";
                                }
                                if(guia.equals(""))
                                    guia = "no informado";
                                System.out.println("Hola! "+encabezadoArray[1]);
                                encabeza.put("Ciudad Receptor", encabezadoArray[8]);
                                datoexterno = encabezadoArray[13];
                                break;
                            case 5:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Fecha Vencimiento", encabezadoArray[1]);
                                final String OLD_FORMATT = "dd-MM-yy";
                                final String NEW_FORMATT = "yyyy-MM-dd";
                                SimpleDateFormat sdff = new SimpleDateFormat(OLD_FORMATT);
                            Date dd = sdff.parse(encabeza.get("Fecha Vencimiento"));
                            sdff.applyPattern(NEW_FORMATT);
                            String newDateStringg = sdff.format(dd);
                                encabeza.put("Fecha Vencimiento", newDateStringg);
                                
                                break;
                            case 6:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                detalle=procesadetalle(encabezadoArray);
                                break;
                            case 7:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                break;
                            case 8:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Descuento", encabezadoArray[1]);
                                break;
                            case 9:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                break;
                            case 10:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Monto Neto", encabezadoArray[0]);
                                encabeza.put("Monto Iva", encabezadoArray[2]);
                                break;
                            case 11:
                                encabezadoArray=procesatabla(bean.getContenido().replaceAll("&nbsp;",""),i);
                                encabeza.put("Monto Total", encabezadoArray[0]);
                                break;
                            default:
                                break;
                            
                        }    
                            
                        
                    }
               
                
                
                    
                    
                
                        
			try {
			   
				// Variables Libres
                                bean.setCodigokaeser("");
                                String condicion=null;
                                if(encabeza.get("Forma de pago").equals("2"))
                                    condicion=condicionpago;
				String varLibre = "";
				varLibre += "A0;";
				varLibre += condicionpago+";";
				varLibre += dias+";";
				varLibre += ""+";";
				varLibre += ""+";";
				varLibre += ""+";";
				varLibre += ""+";";
				varLibre += ";";
				varLibre += ""+";";
				varLibre += ""+";";
				varLibre += ""+";";
				varLibre += "\n";

				// Se asigna a folio
				bean.setRutEmisor("76004392-3");
				
				bean.setSucursalEmisorDTE("999999999");

				String encabezado = "";
				encabezado += "A;";
				
				 
				encabezado += bean.getTipoDTE()+";"; 								// pos_1_Tipo
																// Electrï¿½nico
				encabezado += ";"; 			// pos_2_Tipo Impresiï¿½n
				encabezado += arreglaFolio(bean.getFolioDTE()).trim() + ";"; 							// pos_3_FOLIO-Documento


				encabezado += encabeza.get("Fecha Emision")+ ";"; // pos_4_Fecha
																					// de
																					// Emisiï¿½n
				encabezado += ";"; // pos_5_Indicador de No Rebaja
				encabezado += ";"; // pos_6_Tipo Despacho
                                
				encabezado +=  ";"; // pos_7_Indicador Tipo de Translado de bienes
				encabezado +=  ";"; // pos_8_Indicador Servicio
				encabezado +=  ";"; // pos_9_Indicador
																					// Montos
																					// Brutos
				encabezado += encabeza.get("Forma de pago")+";"; // pos_10_Forma de Pago
				encabezado += "" + ";"; // pos_11_Fecha de Cancelaciï¿½n
				encabezado += ";"; // pos_12_Periodo desde
				encabezado +=  ";"; // pos_13_Periodo hasta
				encabezado += "" + ";"; // pos_14_Medio de Pago
				encabezado += ""
                                        + ";"; // pos_15_Tï¿½rmino de Pago, cï¿½digo
				encabezado += 
						";"; // pos_16_Tï¿½rmino de Pago, dï¿½as
				encabezado += encabeza.get("Fecha Vencimiento")+";"; // pos_17_Fecha  de Vencimiento

				encabezado += bean.getRutEmisor() + ";"; // pos_18_RUT Emisor
				encabezado += "EINHELL (CHILE) S.A." + ";"; // pos_19_Nombre o Razï¿½n  Social Emisor
				encabezado += "COMERCIALIZ.DE MAQUINARIA,HERRAMIENTAS,EQUIPO Y MATERIALES" + ";"; // pos_20_Giro del Negocio del Emisor
				encabezado += "" + ";"; // pos_21_Sucursal
				encabezado += ""
						+ ";"; // pos_22_Cï¿½digo Sucursal
				encabezado += "RECOLETA 1223" + ";"; // pos_23_Direcciï¿½n Origen
                                encabezado += "RECOLETA"
						+ ";"; // pos_24_Comuna Origen
				encabezado += "SANTIAGO"
						+ ";"; // pos_25_Ciuidad Origen
				encabezado += ""
						+ ";"; // pos_26_Cï¿½digo del Vendedor

				encabezado += "" + ";"; // pos_27_RUT Mandante

				encabezado += encabeza.get("Rut Receptor").replace(".", "")
						+ ";"; // pos_28_RUT Receptor
				encabezado += encabeza.get("Codigo Cliente")
						+ ";"; // pos_29_Cï¿½digo Interno del Receptor
				encabezado += encabeza.get("Razon Social Receptor")
						+ ";"; // pos_30_Nombre o Razï¿½n Social Receptor
				encabezado += guia
						+ ";"; // pos_31_Giro del Negocio del Receptor
				encabezado += encabeza.get("Telefono")
						+ ";"; // pos_32_Contacto receptor
				encabezado += encabeza.get("Direccion Receptor")
						+ ";"; // pos_33_Direcciï¿½n Receptor
				encabezado += encabeza.get("Comuna Receptor")
						+ ";"; // pos_34_Comuna Receptor
				encabezado += encabeza.get("Ciudad Receptor")
						+ ";"; // pos_35_Ciudad Receptor
				encabezado += ""
						+ ";"; // pos_36_Direcciï¿½n Postal
				encabezado += ""
						+ ";"; // pos_37_Comuna Postal
				encabezado += ""
						+ ";"; // pos_38_Ciudad Postal

				encabezado += "" + ";"; // pos_39_RUT	Solicitante de Factura

				encabezado += ""
						+ ";"; // pos_40_Informaciï¿½n Transporte (Patente)
				encabezado += ""
						+ ";"; // pos_41_RUT Transportista
				encabezado +=""
						+ ";"; // pos_42_Direcciï¿½n Destino
				encabezado += ""
						+ ";"; // pos_43_Comuna Destino
				encabezado += ""
						+ ";"; // pos_44_Ciudad Destino

				encabezado += encabeza.get("Monto Neto").replace(".","") + ";"; // pos_45_Monto Neto
				encabezado += "" + ";"; // pos_46_Monto No Afecto o Exento
				encabezado += "" + ";"; // pos_47_Monto base faenamiento carne
				encabezado += "19" + ";"; // pos_48_Tasa IVA
				encabezado += encabeza.get("Monto Iva").replace(".","") + ";"; // pos_49_IVA
				encabezado += "" + ";"; // pos_50_IVA no Retenido
				encabezado += "" + ";"; // pos_51_Crï¿½dit especial 65% Empresas Contructoras
				encabezado += ""
						+ ";"; // pos_52_Monto Perï¿½odo
				encabezado += "" + ";"; // pos_53_Garantia por deposito o envases o embalajes
				encabezado += "" + ";"; // pos_54_Monto No Facturable
				encabezado += encabeza.get("Monto Total").replace(".","") + ";"; // pos_55_MontoTotal
				encabezado += ""
						+ ";"; // pos_56_Saldo Anterior
				encabezado += "" + ";"; // pos_57_Valor a pagar
				encabezado += "\n";

				// ACTECO
				encabezado += "A1;";
				encabezado += "515009" + ";";
				encabezado += "\n";

				String detalles = "";
				contador=1;
				for (Map<String, String> map: detalle) {
//					if (detalle.valueOf("IndExe").contains("2"))
//						continue;
					
					
					detalles += "B;";
					detalles += contador + ";"; // pos_1_Nï¿½ de Lï¿½nea o Nï¿½ Secuencial
					detalles += map.get("Indicador Exencion") + ";"; // pos_2_Indicador de facturaciï¿½n/ exenciï¿½n
					detalles += map.get("Numero Parte") + ";"; // pos_3_Nombre del ï¿½tem
					detalles += map.get("Descripcion") + ";";  //pos_4_Descripciï¿½n Adicional
					detalles += map.get("Cantidad Referencia") + ";"; // pos_5_Cantidad  de Referencia
					detalles += map.get("Unidad Referencia") + ";"; // pos_6_Unidad de Medida de Referencia
					detalles += map.get("Precio Referencia") + ";"; // pos_7_Precio de Referencia
					detalles += map.get("Cantidad").replace(".","") + ";"; // pos_8_Cantidad del ï¿½tem
					detalles += map.get("Fecha Elaboracion") + ";"; // pos_9_Fecha Eleboraciï¿½n
					detalles += map.get("Fecha Vencimiento") + ";"; // pos_10_Fecha Vencimiento
					detalles += map.get("Sales Unit") + ";"; // pos_11_Unidad de Medida
					detalles += map.get("Unit Price") + ";"; // pos_12_Precio Unitario del ï¿½tem
					detalles += map.get("Precio Unitario en  Otra Moneda") + ";"; // pos_13_Precio Unitario en Otra Moneda
					detalles += map.get("Codigo de Otra Moneda") +";"; // pos_14_Cï¿½digo de Otra Moneda
					detalles += map.get("Factor de Conversion a pesos") +";"; // pos_15_Factor de Conversiï¿½n
					detalles += map.get("Discount porcent") + ";"; // pos_16_Descuento // en %
					detalles += map.get("Discoun amount") + ";"; // pos_17_Monto  Descuento
					detalles += map.get("Porcentaje de  Recargo") + ";"; // pos_18_Recargo en %
					detalles += map.get("Monto del Recargo") + ";"; // pos_19_Monto de Recargo
					detalles += map.get("Codigo de Impuesto Adicional") + ";"; // pos_20_Cï¿½digo Impuesto o Retenciones
					detalles += map.get("Total ampount") + ";"; // pos_21_Monto // de ï¿½tem
					detalles += "\n";
                                        detalles += "B2;Interno;"+map.get("Codigo Producto") + ";";
                                        detalles += "\n";
							
					contador++;
				}
				String descripcion = "";
				String nombre2 = "";
				String repeticion = "";
                                String recargosDescuentos = "";
                                if(!encabeza.get("Descuento").equals("0")){

				
                                

					recargosDescuentos += "C;" ;
					recargosDescuentos += "1" + ";"; // pos_1_Nï¿½
																				// de
																				// Lï¿½nea
																				// o
																				// Nï¿½
																				// Secuencial
					recargosDescuentos += "D" + ";"; // pos_2_Tipo
																			// de
																			// Movimiento
					recargosDescuentos += "Descuento" + ";"; // pos_3_Glosa
					recargosDescuentos += "$" + ";"; // pos_4_Tipo
																				// de
																				// Valor
					recargosDescuentos += encabeza.get("Descuento").replace(".","") + ";"; // pos_5_Valor
					recargosDescuentos += "" + ";"; // pos_6_Indicador
																				// de
																				// facturaciï¿½n/
																				// exenciï¿½n
					recargosDescuentos += "\n";
                                }
				
				//Borrar despues de hacer las pruebas
//				if(bean.getFolioDTE().equals("61")){
//					if (referenciaArray[2].equals("801")){
//						referenciaArray[2]="33";
//					}
//				}
				String referencias = "";
                                for(int i=0;i<3;i++){
				switch(i){
                                    case 0:
                                        referencias += "D;";
				referencias += (i+1) + ";"; // pos_1_Nï¿½ de Lï¿½nea
				referencias += 801 + ";"; // pos_2_Tipo
																// Documento de
																// referencia
				referencias +=   ";"; // pos_3_Indicador
																// de Referencia
																// Global
				referencias += datoexterno + ";"; // pos_4_FOLIO-
																// de referencia
				referencias +=   ";"; // pos_5_RUT Otro
																// contribuyente
				referencias +=  fecha + ";"; // pos_6_FECHA de
																// la Referencia
				referencias +=   ";"; // pos_7_Cï¿½digo de
																// referencia
				referencias +=   ";"; // pos_8_Razï¿½n
																// referencia
				referencias += "\n";
                                        break;
                                    case 1:
                                        referencias += "D;";
				referencias += (i+1) + ";"; // pos_1_Nï¿½ de Lï¿½nea
				referencias += 802 + ";"; // pos_2_Tipo
																// Documento de
																// referencia
				referencias +=   ";"; // pos_3_Indicador
																// de Referencia
																// Global
				referencias += notaventa + ";"; // pos_4_FOLIO-
																// de referencia
				referencias +=   ";"; // pos_5_RUT Otro
																// contribuyente
				referencias +=   fecha + ";"; // pos_6_FECHA de
																// la Referencia
				referencias +=   ";"; // pos_7_Cï¿½digo de
																// referencia
				referencias +=   ";"; // pos_8_Razï¿½n
																// referencia
				referencias += "\n";
                                        break;
                                    case 2:
                                        if(guia2.equals(""))
                                        {}
                                        else{
                                        referencias += "D;";
				referencias += (i+1) + ";"; // pos_1_Nï¿½ de Lï¿½nea
				referencias += 52 + ";"; // pos_2_Tipo
																// Documento de
																// referencia
				referencias +=   ";"; // pos_3_Indicador
																// de Referencia
																// Global
				referencias += guia2 + ";"; // pos_4_FOLIO-
																// de referencia
				referencias +=   ";"; // pos_5_RUT Otro
																// contribuyente
				referencias += fecha+  ";"; // pos_6_FECHA de
																// la Referencia
				referencias +=   ";"; // pos_7_Cï¿½digo de
																// referencia
				referencias +=   ";"; // pos_8_Razï¿½n
																// referencia
				referencias += "\n";
                                        }
                                break;
                                    default:
                                        break;
                                }
			
                                }
				txt = varLibre + encabezado + detalles +recargosDescuentos+referencias ;
			   
			}
			finally {
			    try {
			        if(br != null)
			            br.close();
			    }
			    catch (Exception e) {
			        System.out.println("Error al cerrar el fichero");
			        System.out.println(e.getMessage());
			    }
			}


			
			
			

		} catch (Exception e) {
			throw new Exception("ERROR al parserar HTML: " + e.getMessage(), e);
		}

		bean.setTXT(txt);
	}

	
	
        public static Collection<Map<String, String>> procesadetalle(String[] procesa){
            Collection<Map<String, String>> detalle = new ArrayList<Map<String, String>> ( );
            int i=0;
            while(i<procesa.length){
                if(procesa[i]!=null){
                Map<String, String> det = new HashMap<String, String>();
							det.put("Numero Linea", "");
							det.put("Indicador Exencion", "");
                                                        det.put("Codigo Producto", procesa [i+2]);
							det.put("Numero Parte", procesa[i+3].replace(";",","));
							det.put("Descripcion", "" + procesa[i+4].replace(";",","));
							det.put("Cantidad Referencia", "" );
							det.put("Unidad Referencia", "" );	
							det.put("Precio Referencia", "" );
							det.put("Cantidad", "" + procesa[i]);
							det.put("Fecha Elaboracion", "");
							det.put("Fecha Vencimiento", "");
								det.put("Sales Unit", "" );
								det.put("Unit Price", procesa[i+5] );
								det.put("Precio Unitario en  Otra Moneda", "" );
								det.put("Codigo de Otra Moneda", "");
								det.put("Factor de Conversion a pesos", "" );
								det.put("Discount porcent", "" );
								det.put("Discoun amount", "" );
								det.put("Porcentaje de  Recargo", "" );
								det.put("Monto del Recargo", "" );
								det.put("Codigo de Impuesto Adicional", "" );
								det.put("Total ampount", procesa[i+6]);
						detalle.add(det);
                                                i=i+7;
                                                System.out.println(i);
                }else break;
            }
            return detalle;
        }
        
        public static  String[] procesatabla(String tabla, int posicion){
            String[] retorna= new String[1000];
            org.jsoup.nodes.Document document = Jsoup.parse(tabla);
                    
                    Elements tables = document.select("body");     
                    Element table = tables.get(0);

                    // Checks if a table contains table inside it
                    while(! table.select(":has(table)").isEmpty()){
                        table = table.select("table table").first();
                       
                    }
                    Elements probando = table.siblingElements();
                    int contando =0;
                    for (Element element : probando){
                        contando++;
                            
                        int contandoparaelswitch =0;
                        int paraelfor=0;
                        
                        int casoespecial=0;
                        if (contando==posicion){
                        switch(contando){
                            case 1:
                                 paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 2:
                                paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 3:
                                 paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 4:
                                paraelfor=0;
                                int lalala=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    
                                    if(contandoparaelswitch%2 == 1){
                                        System.out.println(intento.select("td").text());
                                        retorna[paraelfor]=intento.select("td").text();
                                        paraelfor++;
                                    }
//                                    if(contandoparaelswitch==(14+(16*casoespecial))){
//                                        contandoparaelswitch=contandoparaelswitch+2;
//                                        casoespecial=casoespecial+1;
                                    else{
                                    contandoparaelswitch++;
                                   
                                    }
                                }
                                break;
                            case 5:
                                 paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 6:
                                 paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
   
                                        retorna[paraelfor]=intento.select("td").text();
                                        paraelfor++;
                                    }
                                    if(contandoparaelswitch==(14+(16*casoespecial))){
                                        contandoparaelswitch=contandoparaelswitch+2;
                                        casoespecial=casoespecial+1;
                                    }else{
                                    contandoparaelswitch++;
                                   
                                    }
                                }
                                break;
                            case 7:
                                 paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
//                                }
                                break;
                            case 8:
                                paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 9:
                                paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 10:
                                paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                            case 11:
                                paraelfor=0;
                                for (Element intento : element.getElementsByTag("td")){
                                    if(contandoparaelswitch%2 == 1){
                                    retorna[paraelfor]=intento.select("td").text();
                                    paraelfor++;
                                    }
                                    contandoparaelswitch++;
                                    
                                }
                                break;
                         
                            
                            default:
                                break;
                            
                        }    
                        } 
                        
                    }
            return retorna;
        }
        public static String arreglaFolio(String folio){
            int cantidad= folio.length();
            for (int i= 0;i<cantidad;i++){
                if(folio.startsWith("0")){
                    folio=folio.substring(1);
                }
            }
            return folio;
        }
        public static String retornaFolio(String tabla){
            String folio=null;
            org.jsoup.nodes.Document document = Jsoup.parse(tabla);
                    
                    Elements tables = document.select("body");     
                    Element table = tables.get(0);

                    // Checks if a table contains table inside it
                    while(! table.select(":has(table)").isEmpty()){
                        table = table.select("table table").first();
                       
                    }
                    //System.out.println(table.text());
                    
                    folio=table.select("td").text();
            return folio;
        }

}
