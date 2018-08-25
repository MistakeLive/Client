package mp3player;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class MP3PlayerGUI extends javax.swing.JFrame {


    MainClass MC = new MainClass();

    int count = 0;

    Vector sb;

    public MP3PlayerGUI() {
        sb = getBase();
        initComponents();
    }

    private void initComponents() {

        Stop = new javax.swing.JLabel();
        SelectFile = new javax.swing.JLabel();
        Play = new javax.swing.JLabel();
        Pause = new javax.swing.JLabel();
        Display = new javax.swing.JLabel();
        Background = new javax.swing.JLabel();
        songTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane(songTable);
        textField = new javax.swing.JTextField();
        Find = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Stop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                StopMouseReleased(evt);
            }
        });
        getContentPane().add(Stop, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 43, 60, 70));

        SelectFile.setText("jLabel1");
        SelectFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                try {
                    SelectFileMouseReleased(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getContentPane().add(SelectFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 40, 20));

        Play.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                PlayMouseReleased(evt);
            }
        });
        getContentPane().add(Play, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 34, 90, 90));

        Pause.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                PauseMouseReleased(evt);
            }
        });
        getContentPane().add(Pause, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 54, 60, 60));

        Display.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        getContentPane().add(Display, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 10, 310, 20));

        textField.setFont(new java.awt.Font("Times New Roman", 0, 14));
        getContentPane().add(textField, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 270, 25));

        Find.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FindMouseReleased(evt);
            }
        });
        Find.setFont(new java.awt.Font("Times New Roman", 0, 14));
        Find.setText("Найти");
        getContentPane().add(Find, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 80, 25));

        DefaultTableModel tdm = new DefaultTableModel();

        String [] st  = new String [] {"Song Name"};

        tdm.setColumnIdentifiers(st);

        for (int i=0; i < count; i++){
            Vector data_rows = new Vector();
            data_rows.addElement(sb.elementAt(i));
            tdm.addRow(data_rows);
        }

        songTable.setModel(tdm);

        jScrollPane1.setViewportView(songTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 350, 240));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mp3player/Background.png"))); // NOI18N
        Background.setAutoscrolls(true);
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        songTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                songTableMouseClicked(evt);
            }
        });
        pack();
    }// </editor-fold>

    private void StopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StopMouseReleased
        MC.Stop();
    }//GEN-LAST:event_StopMouseReleased

    private void PlayMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayMouseReleased
        MC.Resume();
    }//GEN-LAST:event_PlayMouseReleased

    private void PauseMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PauseMouseReleased
        MC.Pause();
    }//GEN-LAST:event_PauseMouseReleased

    private void SelectFileMouseReleased(java.awt.event.MouseEvent evt) throws Exception {//GEN-FIRST:event_SelectFileMouseReleased
        FileFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");

        JFileChooser chooser = new JFileChooser("C:\\");
        chooser.addChoosableFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File myFile = chooser.getSelectedFile();

            PostMethod filePost = new PostMethod("http://localhost:8080/");
            Part[] parts = { new FilePart("file", myFile) };
            filePost.setRequestEntity(new MultipartRequestEntity( parts,
                    filePost.getParams()));
            HttpClient client = new HttpClient();
            int status = client.executeMethod(filePost);

            DefaultTableModel model = (DefaultTableModel) songTable.getModel();
            model.addRow(new Object[] {myFile.getName()});

        }
    }//GEN-LAST:event_SelectFileMouseReleased

    private void FindMouseReleased(java.awt.event.MouseEvent evt) {
        String request = textField.getText();
        DefaultTableModel tdm = new DefaultTableModel();

        String [] st  = new String [] {"Song Name"};

        tdm.setColumnIdentifiers(st);

        for (int i=0; i < count; i++){
            Vector data_rows = new Vector();
            if (sb.elementAt(i).toString().lastIndexOf(request) != (-1)) {
                data_rows.addElement(sb.elementAt(i));
                tdm.addRow(data_rows);
            }
        }

        songTable.setModel(tdm);
    }

    private void songTableMouseClicked(java.awt.event.MouseEvent evt) {

        MC.Stop();

        int rowIndex = songTable.getSelectedRow();

        TableModel model = songTable.getModel();

        String name = String.valueOf(model.getValueAt(rowIndex, 0).toString());


        byte[] file = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{

            String uri = "http://localhost:8080/songs/" + name.replace(" ", "%20");
            HttpGet get = new HttpGet(uri);
            HttpResponse response = httpClient.execute(get);

            file = IOUtils.toByteArray(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        Display.setText(name);
        MC.Play(file);

    }

    private Vector getBase(){
        Vector names = new Vector(count);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{

            HttpGet get = new HttpGet("http://localhost:8080/songs");
            HttpResponse response = httpClient.execute(get);

            String json = IOUtils.toString(response.getEntity().getContent());

            StringTokenizer st = new StringTokenizer(json, "\",][");
            while (st.hasMoreTokens()){
                names.add(st.nextToken());
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        IOUtils.closeQuietly(httpClient);
    }
        return names;
    }

    public static void main(String args[]) throws Exception {
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
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */

        Socket s = new Socket("127.0.0.1", 8080);
        System.out.println("connected");

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MP3PlayerGUI().setVisible(true);
            }
        });
    }

    public static javax.swing.JLabel Background;
    private javax.swing.JLabel Display;
    private javax.swing.JLabel Pause;
    private javax.swing.JLabel Play;
    private javax.swing.JLabel SelectFile;
    private javax.swing.JLabel Stop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable songTable;
    private javax.swing.JTextField textField;
    private javax.swing.JButton Find;

}
