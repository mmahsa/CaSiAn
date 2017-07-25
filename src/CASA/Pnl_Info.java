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

/**
 *
 * @author mahsa.moein
 */
public class Pnl_Info extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public Pnl_Info() {
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

        jPnlPlotInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLblSlope = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLblMeanTav = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtPeakNo = new javax.swing.JTextField();
        jBtnFilter = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(300, 140));
        setSize(new java.awt.Dimension(300, 140));

        jPnlPlotInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlPlotInfo.setPreferredSize(new java.awt.Dimension(350, 140));
        jPnlPlotInfo.setSize(new java.awt.Dimension(350, 140));

        jLabel1.setText("Slope:");

        jLblSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblSlope.setPreferredSize(new java.awt.Dimension(70, 25));
        jLblSlope.setSize(new java.awt.Dimension(70, 25));

        jLabel2.setText("Mean Tav:");

        jLblMeanTav.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblMeanTav.setPreferredSize(new java.awt.Dimension(70, 25));
        jLblMeanTav.setSize(new java.awt.Dimension(70, 25));

        jLabel3.setText("Peak No:");

        jTxtPeakNo.setSize(new java.awt.Dimension(70, 25));

        jBtnFilter.setText("Filter signals");
        jBtnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPnlPlotInfoLayout = new javax.swing.GroupLayout(jPnlPlotInfo);
        jPnlPlotInfo.setLayout(jPnlPlotInfoLayout);
        jPnlPlotInfoLayout.setHorizontalGroup(
            jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPnlPlotInfoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLblSlope, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLblMeanTav, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlPlotInfoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtPeakNo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 44, Short.MAX_VALUE))
        );
        jPnlPlotInfoLayout.setVerticalGroup(
            jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jLblSlope, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLblMeanTav, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlPlotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTxtPeakNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBtnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jPnlPlotInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 152, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPnlPlotInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLblMeanTav;
    private javax.swing.JLabel jLblSlope;
    private javax.swing.JPanel jPnlPlotInfo;
    private javax.swing.JTextField jTxtPeakNo;
    // End of variables declaration//GEN-END:variables
}