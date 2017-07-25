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
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author mahsa.moein
 */
public class FDefineTreatmentName extends javax.swing.JFrame {

    DefaultTableModel model;
    private int NumberOfTreatments;
    public ArrayList<String> TreatmentNames;
            
    public FDefineTreatmentName(int TreatmentsNo,ArrayList<String> TreatNames) {
        
        initComponents();
        setLocationRelativeTo(null);
        
        this.setAlwaysOnTop (true);
        
        model = (DefaultTableModel)tblSections.getModel();
        this.NumberOfTreatments = TreatmentsNo;
        this.TreatmentNames = TreatNames;
        
        lblSectionNo.setText("Please define " + String.valueOf(NumberOfTreatments+1) + " sections.");
        
        if(this.TreatmentNames.size()>0)
        {
            LoadDataInTabel();
        }
        
//        setDefaultCloseOperation(FDefineTreatmentName.DO_NOTHING_ON_CLOSE);
//        this.addWindowListener(new java.awt.event.WindowAdapter() {
//        @Override
//        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//
//            if(TreatmentNames.size() == (NumberOfTreatments+1))
//            {
//                setDefaultCloseOperation(FDefineTreatmentName.DISPOSE_ON_CLOSE);
//            }
//        } }); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlDefineName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtSectionName = new javax.swing.JTextField();
        jBtnAdd = new javax.swing.JButton();
        jBtnEdit = new javax.swing.JButton();
        jBtnDelete = new javax.swing.JButton();
        lblSectionNo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSections = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        jPnlDefineName.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Section name:");

        jTxtSectionName.setPreferredSize(new java.awt.Dimension(120, 28));
        jTxtSectionName.setSize(new java.awt.Dimension(120, 28));

        jBtnAdd.setText("Add");
        jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAddActionPerformed(evt);
            }
        });

        jBtnEdit.setText("Edit");
        jBtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditActionPerformed(evt);
            }
        });

        jBtnDelete.setText("Delete");
        jBtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDeleteActionPerformed(evt);
            }
        });

        lblSectionNo.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        lblSectionNo.setForeground(new java.awt.Color(255, 51, 51));
        lblSectionNo.setText("jLabel2");

        javax.swing.GroupLayout jPnlDefineNameLayout = new javax.swing.GroupLayout(jPnlDefineName);
        jPnlDefineName.setLayout(jPnlDefineNameLayout);
        jPnlDefineNameLayout.setHorizontalGroup(
            jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlDefineNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSectionNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlDefineNameLayout.createSequentialGroup()
                        .addGroup(jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlDefineNameLayout.createSequentialGroup()
                                .addComponent(jBtnAdd)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlDefineNameLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtSectionName, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPnlDefineNameLayout.createSequentialGroup()
                                .addComponent(jBtnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBtnDelete)))))
                .addContainerGap())
        );

        jPnlDefineNameLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jBtnAdd, jBtnDelete, jBtnEdit});

        jPnlDefineNameLayout.setVerticalGroup(
            jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlDefineNameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSectionNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTxtSectionName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPnlDefineNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnDelete)
                    .addComponent(jBtnEdit)
                    .addComponent(jBtnAdd))
                .addContainerGap())
        );

        tblSections.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Section name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSections.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSectionsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSections);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPnlDefineName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlDefineName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAddActionPerformed
       
        
        if(model.getRowCount()<= NumberOfTreatments)
        {
             model.insertRow(model.getRowCount(),new Object[]{jTxtSectionName.getText()});
             TreatmentNames.add(jTxtSectionName.getText());           
             jTxtSectionName.requestFocus();
        }
        
        if(model.getRowCount()== NumberOfTreatments+1)
        {
            jBtnAdd.setEnabled(false);
            lblSectionNo.setText("No more section can be added");
        }
        
         jTxtSectionName.setText("");
    }//GEN-LAST:event_jBtnAddActionPerformed

    private void tblSectionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSectionsMouseClicked
        
        jTxtSectionName.setText(String.valueOf(model.getValueAt(tblSections.getSelectedRow(),0)));
        jTxtSectionName.requestFocus();
        
    }//GEN-LAST:event_tblSectionsMouseClicked

    private void jBtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditActionPerformed
        
        if(tblSections.getSelectedRow()>-1 && !"".equals(jTxtSectionName.getText()))
        {
            model.setValueAt(jTxtSectionName.getText(),tblSections.getSelectedRow(),0);
            TreatmentNames.set(tblSections.getSelectedRow(),jTxtSectionName.getText());
            jTxtSectionName.setText("");
        }
    }//GEN-LAST:event_jBtnEditActionPerformed

    private void jBtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDeleteActionPerformed
       if(tblSections.getSelectedRow()>-1)
       {
           TreatmentNames.remove(tblSections.getSelectedRow()); 
           model.removeRow(tblSections.getSelectedRow());
           jBtnAdd.setEnabled(true);
           jTxtSectionName.setText("");
           lblSectionNo.setText("Please define " + String.valueOf(NumberOfTreatments+1) + " sections.");
       }
    }//GEN-LAST:event_jBtnDeleteActionPerformed

    private void LoadDataInTabel()
    {
        for(int i=0; i<this.TreatmentNames.size();i++)
        {
            model.insertRow(model.getRowCount(),new Object[]{TreatmentNames.get(i)});
        }
    }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAdd;
    private javax.swing.JButton jBtnDelete;
    private javax.swing.JButton jBtnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPnlDefineName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTxtSectionName;
    private javax.swing.JLabel lblSectionNo;
    private javax.swing.JTable tblSections;
    // End of variables declaration//GEN-END:variables

    
}
