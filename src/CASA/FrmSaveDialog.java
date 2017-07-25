/*
 * Copyright (C) 2016 Mahsa Moein
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package CASA;

import CASA.ProgressMonitorDemo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import org.jfree.data.gantt.Task;


/**
 *
 * @author mahsa.moein
 */
public class FrmSaveDialog extends javax.swing.JFrame {

    private JFileChooser fc = null;
    private Experiment exp = null;
    private File DefaultPath = null;
    FrmSigAnalysis FrmTemp = null;
      
    public FrmSaveDialog(FrmSigAnalysis frm, Experiment exp, File SaveDefaultPath) {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(FSigma_Tav.DISPOSE_ON_CLOSE);
        
        fc = new JFileChooser();
        this.exp = exp;
        this.FrmTemp = frm;
        
        this.DefaultPath = SaveDefaultPath;
        if (DefaultPath != null) {
            fc.setCurrentDirectory(DefaultPath);
        }       
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Component[] c = fc.getComponents();
        c[3].setVisible(false);
        jPnlMiddle.setLayout(new BorderLayout());
        jPnlMiddle.add(fc,BorderLayout.CENTER);
        this.pack();
                    
    }
      
     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        jPnlMiddle = new javax.swing.JPanel();
        jPnlDown = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCbxFileFormat = new javax.swing.JComboBox();
        jBtnCancel = new javax.swing.JButton();
        jBtnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Save As:");

        txtFileName.setText("Untitled");
        txtFileName.setPreferredSize(new java.awt.Dimension(260, 29));
        txtFileName.setSize(new java.awt.Dimension(260, 29));

        javax.swing.GroupLayout jPnlTopLayout = new javax.swing.GroupLayout(jPnlTop);
        jPnlTop.setLayout(jPnlTopLayout);
        jPnlTopLayout.setHorizontalGroup(
            jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTopLayout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
        );
        jPnlTopLayout.setVerticalGroup(
            jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTopLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPnlMiddleLayout = new javax.swing.GroupLayout(jPnlMiddle);
        jPnlMiddle.setLayout(jPnlMiddleLayout);
        jPnlMiddleLayout.setHorizontalGroup(
            jPnlMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 730, Short.MAX_VALUE)
        );
        jPnlMiddleLayout.setVerticalGroup(
            jPnlMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );

        jPnlDown.setBackground(new java.awt.Color(204, 204, 204));
        jPnlDown.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setText("File Format:");

        jCbxFileFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entire file as *.XLS", "Entire file as *.CSV", "Entire file as *.TXT" }));
        jCbxFileFormat.setPreferredSize(new java.awt.Dimension(260, 29));
        jCbxFileFormat.setSize(new java.awt.Dimension(260, 29));

        javax.swing.GroupLayout jPnlDownLayout = new javax.swing.GroupLayout(jPnlDown);
        jPnlDown.setLayout(jPnlDownLayout);
        jPnlDownLayout.setHorizontalGroup(
            jPnlDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlDownLayout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCbxFileFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
        );
        jPnlDownLayout.setVerticalGroup(
            jPnlDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlDownLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPnlDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jCbxFileFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBtnCancel.setText("Cancel");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        jBtnSave.setText("Save");
        jBtnSave.setPreferredSize(new java.awt.Dimension(86, 29));
        jBtnSave.setSize(new java.awt.Dimension(86, 29));
        jBtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPnlMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPnlMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPnlDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSaveActionPerformed
        
        if (exp != null) {
            boolean write = false;
            File resultFile = null;
            try {
                if (!write) {                   
                    File folder = this.fc.getSelectedFile();
                    
                    if(DefaultPath != null && folder==null)
                    {
                        folder = DefaultPath;
                    }
                     
                    if(folder!=null)
                    {
                        if (!folder.isDirectory()) {
                            folder = folder.getParentFile();
                        }
                        this.DefaultPath = folder;
                        this.FrmTemp.SaveDefaultPath = folder;
                                
                        if (folder.exists()) {
                            String ResultPath = DefaultPath + "/" + txtFileName.getText().trim();
                             resultFile = new File(ResultPath);

                            if (resultFile.exists()) {
                                int dialogResult = JOptionPane.showConfirmDialog(this, "There is already a file with " + txtFileName.getText().trim() + " name in the selected directory."
                                        + " Do you like to overwrite on it? Otherwise please select anoter directory.");
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    write = true;
                                } else {
                                    write = false;
                                    return;
                                }
                            } else {
                                if (!resultFile.mkdir()) {
                                    JOptionPane.showMessageDialog(this, "Failed to create directory!");
                                    write = false;
                                    return;
                                } else {
                                    write = true;
                                }

                            }
                        }
                    }
                    else
                    {
                       JOptionPane.showMessageDialog(this, "Please identify the destination of your file."); 
                       return;
                    }
                }
                
                if(write && resultFile!=null) {
                                                   
                    Utils.SaveType file_extension = Utils.SaveType.dat;
                    if(jCbxFileFormat.getSelectedItem().toString().contains("XLS")){
                        file_extension = Utils.SaveType.xls; 
                    }
                    if(jCbxFileFormat.getSelectedItem().toString().contains("CSV")){
                        file_extension = Utils.SaveType.csv; 
                    }
                    if(jCbxFileFormat.getSelectedItem().toString().contains("TXT")){
                         file_extension = Utils.SaveType.txt; 
                    }
                    
                    if(jCbxFileFormat.getSelectedItem().toString().contains("DAT")){
                         file_extension = Utils.SaveType.dat;
                    }
                    
                    exp.SaveData(resultFile.getAbsolutePath(), file_extension,txtFileName.getText().trim());
                    
                    exp.SaveSignalPlotsInPDF(resultFile.getAbsolutePath(), 740,220);
                    exp.SaveSigma_TavPlotsInPDF(resultFile.getAbsolutePath(), 400, 300);                   
                    resultFile = null;
                    JOptionPane.showMessageDialog(this, "Data and figures are Saved!"); 
                    this.fc = null;
                    this.setVisible(false);
                }
                
            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, e.toString());

            }
            finally{
                System.gc();
            }
        } else {
            JOptionPane.showMessageDialog(this, " There is no data to save! ");
        }
                
      
    }//GEN-LAST:event_jBtnSaveActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jBtnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnSave;
    private javax.swing.JComboBox jCbxFileFormat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPnlDown;
    private javax.swing.JPanel jPnlMiddle;
    private javax.swing.JPanel jPnlTop;
    private javax.swing.JTextField txtFileName;
    // End of variables declaration//GEN-END:variables
}

