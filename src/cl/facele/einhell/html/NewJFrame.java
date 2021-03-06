/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.facele.einhell.html;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import cl.facele.docele.soap.logica.Soap;
/**
 *
 * @author Sojiroh
 */
public class NewJFrame extends javax.swing.JFrame {
private static Path filePath;
        private static long folio = 1;
            
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setText("Seleccione archivo html...");

        jButton1.setText("Examinar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Generar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(74, 74, 74)
                .addComponent(jButton2)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        jTextField1.setText(file.getAbsolutePath());
        filePath = Paths.get(file.getAbsolutePath());
        
    } else {
        System.out.println("File access cancelled by user.");
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    
     public class CapturePane extends JPanel implements Consumer {

        private JTextArea output;

        public CapturePane() {
            setLayout(new BorderLayout());
            output = new JTextArea();
            add(new JScrollPane(output));
        }

        public void appendText(final String text) {
            if (EventQueue.isDispatchThread()) {
                output.append(text);
                output.setCaretPosition(output.getText().length());
            } else {

                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        appendText(text);
                    }
                });

            }
        }        
    }

    public interface Consumer {        
        public void appendText(String text);        
    }

    public class StreamCapturer extends OutputStream {

        private StringBuilder buffer;
        private String prefix;
        private Consumer consumer;
        private PrintStream old;

        public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
            this.prefix = prefix;
            buffer = new StringBuilder(128);
            buffer.append("[").append(prefix).append("] ");
            this.old = old;
            this.consumer = consumer;
        }

        @Override
        public void write(int b) throws IOException {
            char c = (char) b;
            String value = Character.toString(c);
            buffer.append(value);
            if (value.equals("\n")) {
                consumer.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                buffer.append("[").append(prefix).append("] ");
            }
            old.print(c);
        }        
    }    
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DirectoryStream<Path> directory = null;
		DTEBean bean;
                String logg =null;
		Transforma tr;
                CapturePane capturePane = new CapturePane();
                Soap soap;
                PrintStream ps = System.out;
                System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));
                 System.out.println("Hello, this is a test");
                File folder = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-ok").toString());
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File folder2 = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-error").toString());
                if (!folder2.exists()) {
                    folder2.mkdirs();
                }

		
		try {
			
                                
                                System.out.println(filePath.toString());
				tr = new Transforma();
				bean = new DTEBean();
				soap = new Soap(Soap.SOAP_SFE);
                                File original = new File (filePath.toString());
				bean.setContendioFile(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
				bean.setPathtxt(filePath.toString());		
				bean.setRutEmisor("76004392-3");
                                if(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")).split("<HR>").length > 2){
                                    String[] htm=new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")).split("<HR>");
//                                    System.out.println(htm[0]);
                                    for(int i=0;i<htm.length;i++){
                                        if(i==0){
                                            String abajo="</TD></TR>\n" +"</TABLE>"+"</BODY>\n" +
                                        "</HTML>";
                                            bean = new DTEBean();
                                            if (filePath.toString().contains("NC"))
                                                bean.setTipoDTE("61");
                                            else
                                                bean.setTipoDTE("33");
                                            bean.setRutEmisor("76004392-3");
                                            bean.setContendioFile(htm[0]+abajo);
                                            Transforma.toTXT57(bean);
                                            try{
                                            	System.out.println(bean.getTXT());
                                        soap.generaDTE(bean.getRutEmisor(), "999999999", bean.getTXT());
                                         logg = soap.glosaOperacion;
                                        folio=soap.folioDTE;


                                        Thread.sleep(10);

                                        File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-ok")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+folio+".txt");

                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        bw.write(bean.getTXT());
                                        bw.close();
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        original.delete();
                                }catch (Exception h) {
                                	File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".txt");
                                        File file2 = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".log.txt");
                                         if (!file2.exists()) {
                                                file2.createNewFile();
                                        }
                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw.write(bean.getTXT());
                                        bw.close();
                                        
                                        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
                                        BufferedWriter bw2 = new BufferedWriter(fw2);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw2.write(soap.glosaOperacion);
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        bw2.close();
//                                        original.delete();
                                } 
                                        } else if(i==(htm.length-2)){
                                            htm[i]=htm[i].replaceFirst("</TD></TR>","");
                                            htm[i]=htm[i].replaceFirst("</TABLE>","");
                                            
                                            String abajo="</TD></TR>\n" +"</TABLE>"+"</BODY>\n" +
                                        "</HTML>";
                                            String arriba="<HTML>\n" +
                                            "<HEAD>\n" +
                                            "<TITLE>Factura Einhell Chile</TITLE>\n" +
                                            "</HEAD>\n" +
                                            "<BODY BGCOLOR=ffffff>";
                                            bean = new DTEBean();
                                            if (filePath.toString().contains("NC"))
                                                bean.setTipoDTE("61");
                                            else
                                                bean.setTipoDTE("33");
                                            bean.setRutEmisor("76004392-3");
                                            bean.setContendioFile(arriba+htm[i]+abajo);
                                            Transforma.toTXT57(bean);
                                            try{
                                            	System.out.println(bean.getTXT());
                                        soap.generaDTE(bean.getRutEmisor(), "999999999", bean.getTXT());
                                          logg = soap.glosaOperacion;
                                        folio=soap.folioDTE;
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        byte[] pdf = soap.getStringPDFDTE(bean.getRutEmisor(), 33, folio, false);
                                        


                                        Thread.sleep(10);

                                        File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-ok")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+folio+".txt");

                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        bw.write(bean.getTXT());
                                        bw.close();
                                        original.delete();
                                }catch (Exception h) {
                                	File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".txt");
                                        File file2 = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".log.txt");
                                         if (!file2.exists()) {
                                                file2.createNewFile();
                                        }
                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw.write(bean.getTXT());
                                        bw.close();
                                         FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
                                        BufferedWriter bw2 = new BufferedWriter(fw2);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw2.write(soap.glosaOperacion);
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        bw2.close();
//                                        original.delete();
                                } 
                                            break;
                                        } else {
                                            htm[i]=htm[i].replaceFirst("</TD></TR>","");
                                            htm[i]=htm[i].replaceFirst("</TABLE>","");
                                            
                                            String abajo="</TD></TR>\n" +"</TABLE>"+"</BODY>\n" +
                                        "</HTML>";
                                            String arriba="<HTML>\n" +
                                            "<HEAD>\n" +
                                            "<TITLE>Factura Einhell Chile</TITLE>\n" +
                                            "</HEAD>\n" +
                                            "<BODY BGCOLOR=ffffff>";
                                            bean = new DTEBean();
                                            if (filePath.toString().contains("NC"))
                                                bean.setTipoDTE("61");
                                            else
                                                bean.setTipoDTE("33");
                                            bean.setRutEmisor("76004392-3");
                                            bean.setContendioFile(arriba+htm[i]+abajo);
                                            Transforma.toTXT57(bean);
                                            try{
                                            	System.out.println(bean.getTXT());
                                        soap.generaDTE(bean.getRutEmisor(), "999999999", bean.getTXT());
                                         logg = soap.glosaOperacion;
                                        folio=soap.folioDTE;
                                        jTextArea1.append(soap.glosaOperacion+"\n");

                                        Thread.sleep(10);

                                        File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-ok")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+folio+".txt");

                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        bw.write(bean.getTXT());
                                        bw.close();
                                        original.delete();
                                }catch (Exception h) {
                                    
                                	File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".txt");
                                        File file2 = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".log.txt");
                                         if (!file2.exists()) {
                                                file2.createNewFile();
                                        }
                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw.write(bean.getTXT());
                                        bw.close();
                                         FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
                                        BufferedWriter bw2 = new BufferedWriter(fw2);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw2.write(soap.glosaOperacion);
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        bw2.close();
//                                        original.delete();
                                } 
                                        }
                                    }
                                }else{
                                    if (filePath.toString().toLowerCase().contains("credit")){
                                                bean.setTipoDTE("61");
                                                bean.setContendioFile(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                                Transforma2.toTXT57(bean);
                                    }
                                            else{
                                                bean.setTipoDTE("33");
                                                bean.setContendioFile(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                                Transforma.toTXT57(bean);
                                            }
                                    
                                    try{
                                    	System.out.println(bean.getTXT());
                                        soap.generaDTE(bean.getRutEmisor(), "999999999", bean.getTXT());
                                          logg = soap.glosaOperacion;
                                        folio=soap.folioDTE;
                                        jTextArea1.append(soap.glosaOperacion+"\n");


                                        Thread.sleep(10);

                                        File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt", "proceso-ok")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+folio+".txt");

                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        bw.write(bean.getTXT());
                                        bw.close();
                                        original.delete();
                                }catch (Exception h) {
                                	File file = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".txt");
                                        File file2 = new File(Paths.get(System.getProperty("user.home"), "Facele", "Einhell", "html","txt",  "proceso-error")+"\\"+bean.getRutEmisor()+"_T"+bean.getTipoDTE()+"_F"+bean.getFolioDTE()+".log.txt");
                                         if (!file2.exists()) {
                                                file2.createNewFile();
                                        }
                                        // if file doesnt exists, then create it
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }
                                      
                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw.write(bean.getTXT());
                                        bw.close();
                                         FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
                                        BufferedWriter bw2 = new BufferedWriter(fw2);
//                                        bw.write(new String(Files.readAllBytes(filePath), Charset.forName("ISO-8859-1")));
                                        bw2.write(soap.glosaOperacion);
                                        jTextArea1.append(soap.glosaOperacion+"\n");
                                        bw2.close();
//                                        original.delete();
                                } 
				
                                }
                                System.out.println(filePath.toString());
				
				System.out.println(bean.getTXT());
				
				
				
				
				Thread.sleep(10);
                                

				
			
		} catch (Exception e) {
            try {
                throw new Exception("Error iterando archivos: " + e.getMessage(), e);
            } catch (Exception ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
		} finally {
			try {directory.close();
			} catch (Exception e) {
				Logger.getLogger(NewJFrame.class.getCanonicalName()).log(Level.INFO, e.getMessage());
			}
		}
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
