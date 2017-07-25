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

import java.awt.BorderLayout;

import java.awt.Dimension;

public class FrmFilterSignal extends javax.swing.JFrame {

    private Signal signal;
    private Experiment exp;
    
    public FrmFilterSignal(Experiment exp,Signal sig) {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(FSigma_Tav.DISPOSE_ON_CLOSE);
        this.SetLayout();
        this.signal = sig;
        this.exp = exp;
        this.jTxtFs.setText("100");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlPlotHolder = new javax.swing.JPanel();
        jPnlFreqDomain = new javax.swing.JPanel();
        jPnlFilteredSignal = new javax.swing.JPanel();
        jPnlActions = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jCbRelationalOp = new javax.swing.JComboBox();
        jTxtValue = new javax.swing.JTextField();
        jCBbLogicalOp = new javax.swing.JComboBox();
        jBtnAdd = new javax.swing.JButton();
        jBtnMinus = new javax.swing.JButton();
        jBtnFilter = new javax.swing.JButton();
        jBtnRemoveFilter = new javax.swing.JButton();
        jCbxApplyToAll = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtFs = new javax.swing.JTextField();
        jBtnFFt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Remove Background");

        jPnlPlotHolder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPnlPlotHolder.setSize(new java.awt.Dimension(533, 554));

        javax.swing.GroupLayout jPnlFreqDomainLayout = new javax.swing.GroupLayout(jPnlFreqDomain);
        jPnlFreqDomain.setLayout(jPnlFreqDomainLayout);
        jPnlFreqDomainLayout.setHorizontalGroup(
            jPnlFreqDomainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );
        jPnlFreqDomainLayout.setVerticalGroup(
            jPnlFreqDomainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
        );

        jPnlFilteredSignal.setPreferredSize(new java.awt.Dimension(519, 267));

        javax.swing.GroupLayout jPnlFilteredSignalLayout = new javax.swing.GroupLayout(jPnlFilteredSignal);
        jPnlFilteredSignal.setLayout(jPnlFilteredSignalLayout);
        jPnlFilteredSignalLayout.setHorizontalGroup(
            jPnlFilteredSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );
        jPnlFilteredSignalLayout.setVerticalGroup(
            jPnlFilteredSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPnlPlotHolderLayout = new javax.swing.GroupLayout(jPnlPlotHolder);
        jPnlPlotHolder.setLayout(jPnlPlotHolderLayout);
        jPnlPlotHolderLayout.setHorizontalGroup(
            jPnlPlotHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotHolderLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPnlPlotHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlFreqDomain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlFilteredSignal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPnlPlotHolderLayout.setVerticalGroup(
            jPnlPlotHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlFreqDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPnlFilteredSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPnlActions.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPnlActions.setPreferredSize(new java.awt.Dimension(345, 554));
        jPnlActions.setSize(new java.awt.Dimension(345, 554));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter"));
        jPanel1.setPreferredSize(new java.awt.Dimension(340, 320));
        jPanel1.setSize(new java.awt.Dimension(340, 320));

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jLabel2.setText("Frequency:");

        jCbRelationalOp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { ">", "<", "=" }));
        jCbRelationalOp.setPreferredSize(new java.awt.Dimension(70, 27));
        jCbRelationalOp.setSize(new java.awt.Dimension(70, 27));

        jTxtValue.setPreferredSize(new java.awt.Dimension(80, 28));

        jCBbLogicalOp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "and", "or" }));
        jCBbLogicalOp.setPreferredSize(new java.awt.Dimension(50, 27));
        jCBbLogicalOp.setSize(new java.awt.Dimension(50, 27));

        jBtnAdd.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jBtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/AddPeak.png"))); // NOI18N
        jBtnAdd.setToolTipText("Add");
        jBtnAdd.setPreferredSize(new java.awt.Dimension(40, 35));
        jBtnAdd.setSize(new java.awt.Dimension(40, 35));
        jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAddActionPerformed(evt);
            }
        });

        jBtnMinus.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jBtnMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/minus.png"))); // NOI18N
        jBtnMinus.setPreferredSize(new java.awt.Dimension(40, 35));
        jBtnMinus.setSize(new java.awt.Dimension(40, 35));

        jBtnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/filter.png"))); // NOI18N
        jBtnFilter.setText("Filter");
        jBtnFilter.setPreferredSize(new java.awt.Dimension(80, 28));
        jBtnFilter.setSize(new java.awt.Dimension(80, 29));
        jBtnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFilterActionPerformed(evt);
            }
        });

        jBtnRemoveFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/reset.png"))); // NOI18N
        jBtnRemoveFilter.setText("Reset");
        jBtnRemoveFilter.setPreferredSize(new java.awt.Dimension(70, 29));
        jBtnRemoveFilter.setSize(new java.awt.Dimension(70, 29));

        jCbxApplyToAll.setText("apply to all signals");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnMinus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBtnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnRemoveFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCbxApplyToAll))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(2, 2, 2)
                        .addComponent(jCbRelationalOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBbLogicalOp, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jCbRelationalOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBbLogicalOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnMinus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnRemoveFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCbxApplyToAll))
                .addGap(10, 10, 10))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("FFT"));
        jPanel2.setPreferredSize(new java.awt.Dimension(340, 75));
        jPanel2.setSize(new java.awt.Dimension(340, 75));

        jLabel1.setText("Sampling frequency:");

        jTxtFs.setPreferredSize(new java.awt.Dimension(80, 28));
        jTxtFs.setSize(new java.awt.Dimension(80, 28));

        jBtnFFt.setText("FFT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTxtFs, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnFFt, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTxtFs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnFFt))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPnlActionsLayout = new javax.swing.GroupLayout(jPnlActions);
        jPnlActions.setLayout(jPnlActionsLayout);
        jPnlActionsLayout.setHorizontalGroup(
            jPnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlActionsLayout.createSequentialGroup()
                .addGroup(jPnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPnlActionsLayout.setVerticalGroup(
            jPnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPnlPlotHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPnlPlotHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnAddActionPerformed

    private void jBtnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnFilterActionPerformed
    private void SetLayout() 
    {
        this.setLayout(new BorderLayout());
        this.jPnlActions.setPreferredSize(new Dimension(345,554));
        this.add(this.jPnlActions, BorderLayout.WEST);
        
        jPnlPlotHolder.setPreferredSize(new Dimension(533,554));
        this.add(this.jPnlPlotHolder, BorderLayout.CENTER);

        this.pack();
    }
    
    private void FFt()
    {
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAdd;
    private javax.swing.JButton jBtnFFt;
    private javax.swing.JButton jBtnFilter;
    private javax.swing.JButton jBtnMinus;
    private javax.swing.JButton jBtnRemoveFilter;
    private javax.swing.JComboBox jCBbLogicalOp;
    private javax.swing.JComboBox jCbRelationalOp;
    private javax.swing.JCheckBox jCbxApplyToAll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPnlActions;
    private javax.swing.JPanel jPnlFilteredSignal;
    private javax.swing.JPanel jPnlFreqDomain;
    private javax.swing.JPanel jPnlPlotHolder;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTxtFs;
    private javax.swing.JTextField jTxtValue;
    // End of variables declaration//GEN-END:variables
}
