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
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mahsa.moein
 */
public class FrmRemoveBackgorund extends javax.swing.JFrame {

    private int signal_Index = -1;
    private Experiment exp;
    private SignalPlot sig_plot;
    private ArrayList<SignalTemp> signals_tmp;
    private final JDialog dialog_ref;
   //In this calss we work just on a copy of signals_tmp.
    //every change will be done the copy of signals_tmp , except when the user confirm
    //their changes by clicking on the confirm button.
    
    public FrmRemoveBackgorund(Experiment exp,int sigIndex,JDialog dialog) {
        initComponents();
        this.SetLayout();
        this.dialog_ref = dialog;
        jBtnConfirmClose.setEnabled(false);
        
        dialog_ref.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(dialog_ref, 
                    "Do you want to apply your changes to signal before closing?","",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)){
                ConfirmClose();
            }
            else
            {
                CancelClose();
            }
        }
        
        });
        for (EnumType.CurveType value : EnumType.CurveType.values()) {
            jCbxCurveType.addItem(value);
        }
        jCbxCurveType.setSelectedItem(EnumType.CurveType.None);
        if(exp!=null && sigIndex!=-1)
        {
            this.signal_Index = sigIndex;
            this.exp = exp;
            this.CopySignals();
            jPnlGraphHolder.setLayout(new BorderLayout());
            this.CreateAndShowSignalPlot();
            
            this.ShowSignalPlot();
        }
       
    }
    private void ShowSignalPlot() {
        if(sig_plot!= null)
        {
            jPnlGraphHolder.removeAll(); 
            jPnlGraphHolder.repaint();
            jPnlGraphHolder.add(this.sig_plot.Signalcpanel);            
            jPnlGraphHolder.validate();
            
        }
        else
        {
             JOptionPane.showMessageDialog(this, "Signal cannot be shown!");
        }
    }
    
    private void CreateAndShowSignalPlot()
    {
        this.sig_plot = null;
        this.sig_plot = exp.signals.get(signal_Index).createSignalPlotWithoutPeaksAndSections(this.exp);
        this.ShowSignalPlot();
    }
    
    private void SetLayout() 
    {
        this.setLayout(new BorderLayout());
        this.jPnlControl.setPreferredSize(new Dimension(295,400));
        this.add(this.jPnlControl, BorderLayout.WEST);        
        jPnlGraphHolder.setPreferredSize(new Dimension(600,400));
        this.add(this.jPnlGraphHolder, BorderLayout.CENTER);
        this.pack();
    }
    
    private void CopySignals()
    {        
        this.signals_tmp = null;
        signals_tmp = new ArrayList<SignalTemp>();
        for(int i=0; i< this.exp.signals.size(); i++)
        {
            SignalTemp st = new SignalTemp();
            st.sig_Id = this.exp.signals.get(i).getSig_Id();
            st.sig_time = (ArrayList<Double>)this.exp.signals.get(i).sig_time.clone();
            st.sig_value = (ArrayList<Double>)this.exp.signals.get(i).sig_value.clone();
            signals_tmp.add(st);
        }
        System.gc();
      
    }
    
    private void EstimateBaseLine()
    {                
        EnumType.CurveType selected_curve_type = (EnumType.CurveType)jCbxCurveType.getSelectedItem();
        
        if(selected_curve_type!= EnumType.CurveType.None)
        {
            Signal sig = exp.signals.get(signal_Index); 
            switch(selected_curve_type)
            {
                case ConstantBaseLine:
                {
                    sig.ConstantBaseLine(this.sig_plot);
                    break;
                }
                case SpikeLine:
                {
                    sig.FitSpikeBaseLine(this.sig_plot);
                    break;
                }
                default:
                {
                    sig.FitCurve(selected_curve_type, sig_plot);
                    break;
                }
            }
            
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlControl = new javax.swing.JPanel();
        jCBToAll = new javax.swing.JCheckBox();
        jBtnReset = new javax.swing.JButton();
        jBtnConfirmClose = new javax.swing.JButton();
        jBtnCancelClose = new javax.swing.JButton();
        jCbxCurveType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jCbxNormalize = new javax.swing.JCheckBox();
        jCbxSubtractBG = new javax.swing.JCheckBox();
        jBtnApplyChangesTemp = new javax.swing.JButton();
        jPnlGraphHolder = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Remove background");
        setAlwaysOnTop(true);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        jPnlControl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlControl.setPreferredSize(new java.awt.Dimension(295, 400));
        jPnlControl.setSize(new java.awt.Dimension(295, 400));

        jCBToAll.setText("To all signals");
        jCBToAll.setPreferredSize(new java.awt.Dimension(150, 29));
        jCBToAll.setSize(new java.awt.Dimension(150, 29));

        jBtnReset.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jBtnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/reset.png"))); // NOI18N
        jBtnReset.setText("Reset");
        jBtnReset.setPreferredSize(new java.awt.Dimension(135, 29));
        jBtnReset.setSize(new java.awt.Dimension(135, 29));
        jBtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnResetActionPerformed(evt);
            }
        });

        jBtnConfirmClose.setText(" Confirm & close");
        jBtnConfirmClose.setPreferredSize(new java.awt.Dimension(135, 29));
        jBtnConfirmClose.setSize(new java.awt.Dimension(135, 29));
        jBtnConfirmClose.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jBtnConfirmCloseAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jBtnConfirmClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnConfirmCloseActionPerformed(evt);
            }
        });

        jBtnCancelClose.setText("Cancel & close");
        jBtnCancelClose.setPreferredSize(new java.awt.Dimension(135, 29));
        jBtnCancelClose.setSize(new java.awt.Dimension(135, 29));
        jBtnCancelClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelCloseActionPerformed(evt);
            }
        });

        jCbxCurveType.setPreferredSize(new java.awt.Dimension(160, 27));
        jCbxCurveType.setSize(new java.awt.Dimension(160, 27));
        jCbxCurveType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCbxCurveTypeItemStateChanged(evt);
            }
        });

        jLabel1.setText("Curve type:");

        jCbxNormalize.setText("Normalize");

        jCbxSubtractBG.setText("Subtract background");

        jBtnApplyChangesTemp.setText("Apply");
        jBtnApplyChangesTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnApplyChangesTempActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPnlControlLayout = new javax.swing.GroupLayout(jPnlControl);
        jPnlControl.setLayout(jPnlControlLayout);
        jPnlControlLayout.setHorizontalGroup(
            jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlControlLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlControlLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCbxCurveType, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPnlControlLayout.createSequentialGroup()
                            .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBtnConfirmClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBtnApplyChangesTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBtnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBtnCancelClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPnlControlLayout.createSequentialGroup()
                            .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCbxSubtractBG)
                                .addComponent(jCBToAll, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addComponent(jCbxNormalize))))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPnlControlLayout.setVerticalGroup(
            jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlControlLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCbxCurveType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(14, 14, 14)
                .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCbxSubtractBG)
                    .addComponent(jCbxNormalize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBToAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnApplyChangesTemp)
                    .addComponent(jBtnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnConfirmClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnCancelClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jPnlGraphHolder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPnlGraphHolder.setPreferredSize(new java.awt.Dimension(600, 400));
        jPnlGraphHolder.setSize(new java.awt.Dimension(600, 400));

        javax.swing.GroupLayout jPnlGraphHolderLayout = new javax.swing.GroupLayout(jPnlGraphHolder);
        jPnlGraphHolder.setLayout(jPnlGraphHolderLayout);
        jPnlGraphHolderLayout.setHorizontalGroup(
            jPnlGraphHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );
        jPnlGraphHolderLayout.setVerticalGroup(
            jPnlGraphHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPnlGraphHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlGraphHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    

    private void jCbxCurveTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCbxCurveTypeItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
        {
            if (jCbxCurveType.getItemCount() > 0 && this.signal_Index > -1) {
                if((EnumType.CurveType)jCbxCurveType.getSelectedItem() == EnumType.CurveType.None)
                {
                   CreateAndShowSignalPlot(); 
                }
                else
                {                
                    EstimateBaseLine();
                    this.ShowSignalPlot(); 
                }
            }
        }
    }//GEN-LAST:event_jCbxCurveTypeItemStateChanged

    private void Reset(boolean allSignals)
    {
       
        if(allSignals)
        {
            for(int i=0; i< exp.signals.size(); i++)
            {
                SignalTemp sig_temp = signals_tmp.get(i);
                Signal sig = exp.signals.get(i);
                
                if((sig.IsBackgroundRemoved_temp || sig.IsNormalized_temp)&& 
                        !sig.SigBGRemoved && !sig.IsNormalized)
                {
                    for(int j=0; j< sig.sig_time.size(); j++ )
                    {
                        sig.sig_value.set(j, sig_temp.sig_value.get(j));
                    }
                    sig.resetBackgroundFlags();
                    EnableCompoents();
                }               
            }
        }
        else
        {
           SignalTemp sig_temp = signals_tmp.get(signal_Index);
           Signal sig = exp.signals.get(signal_Index);
           if((sig.IsBackgroundRemoved_temp || sig.IsNormalized_temp)&& 
                        !sig.SigBGRemoved && !sig.IsNormalized)
           {
                for(int i=0; i< sig.sig_time.size(); i++)
                {
                    sig.sig_value.set(i, sig_temp.sig_value.get(i));
                }
                sig.resetBackgroundFlags();
                EnableCompoents();
           }
        }        
        EnableCompoents();        
    }
    
    private void EnableCompoents(){
        jCbxCurveType.setEnabled(true);
        jCbxCurveType.setSelectedItem(EnumType.CurveType.None); 
        jCBToAll.setEnabled(true);
        jCbxSubtractBG.setEnabled(true);
        jCbxNormalize.setEnabled(true);
        jBtnConfirmClose.setEnabled(false);
    }
    
    private void CancelClose()
    {
       this.Reset(true);
       this.dialog_ref.dispose();
       
    }
    private void ConfirmClose(){
        if(!jCBToAll.isSelected())
        {           
           Signal sig = exp.signals.get(signal_Index);
           if(sig.IsBackgroundRemoved_temp || sig.IsNormalized_temp)
           {
               sig.EditSectionValuesByChangingSignalValues(); 
           }
        }
        else
        {
           for(Signal sig:exp.signals)
           {
               if(sig.IsBackgroundRemoved_temp || sig.IsNormalized_temp)
               {
                   sig.EditSectionValuesByChangingSignalValues(); 
               }
           }
        }
        this.dialog_ref.dispose();
    }
    
    private void jBtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnResetActionPerformed
        // TODO add your handling code here:
        this.Reset(jCBToAll.isSelected());
        CreateAndShowSignalPlot();
    }//GEN-LAST:event_jBtnResetActionPerformed

    private void jBtnConfirmCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConfirmCloseActionPerformed
       ConfirmClose();                
    }//GEN-LAST:event_jBtnConfirmCloseActionPerformed

    
    private void jBtnCancelCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelCloseActionPerformed
        
        CancelClose();
       
    }//GEN-LAST:event_jBtnCancelCloseActionPerformed

    private void jBtnApplyChangesTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnApplyChangesTempActionPerformed
        // TODO add your handling code here:
        boolean IsBGRemoved = false;
        boolean IsNormalized = false;
        EnumType.CurveType selected_curve_type = (EnumType.CurveType)jCbxCurveType.getSelectedItem();
        if(!jCBToAll.isSelected())
        {
            Signal sig = exp.signals.get(signal_Index); 
            if(jCbxSubtractBG.isSelected() &&  !sig.IsNormalized_temp && !sig.SigBGRemoved && !sig.IsBackgroundRemoved_temp) 
            {
                if(sig.subtractBackgrund())
                {
                    jCbxSubtractBG.setSelected(false);
                    jCbxSubtractBG.setEnabled(false);
                    IsBGRemoved = true;
                    
                }
            }
            if(jCbxNormalize.isSelected() && !sig.IsNormalized && !sig.IsNormalized_temp)
            {
                if(sig.Normalize())
                {
                    jCbxNormalize.setSelected(false);
                    jCbxNormalize.setEnabled(false);
                    jCbxSubtractBG.setEnabled(false);
                    jCBToAll.setEnabled(false);
                    jCBToAll.setSelected(false);
                    IsNormalized = true;
                    
                }
            }
        }
        else
        {
            for(Signal sig: exp.signals)
            {
                if(sig.IsPeakComputedForAllSections()) 
                {
                    if(selected_curve_type!= EnumType.CurveType.None)
                    {
                        if(jCbxSubtractBG.isSelected() &&  !sig.IsNormalized_temp && 
                              !sig.SigBGRemoved && !sig.IsBackgroundRemoved_temp) 
                        {
                            if(sig.ComputeRegressionParams(selected_curve_type, this.sig_plot))
                            {                               
                                sig.subtractBackgrund();
                                jCbxSubtractBG.setSelected(false);
                                jCbxSubtractBG.setEnabled(false);
                                IsBGRemoved = true;
                                jCBToAll.setSelected(false);
                               
                            }
                        }
                        
                        if(jCbxNormalize.isSelected() && !sig.IsNormalized && !sig.IsNormalized_temp){
                            sig.Normalize();
                            jCbxNormalize.setSelected(false);
                            jCbxNormalize.setEnabled(false);
                            jCbxSubtractBG.setEnabled(false);
                            jCBToAll.setEnabled(false);
                            jCBToAll.setSelected(false);
                            IsNormalized = true;
                            
                        }
                      
                    }
                }
            }
        }
        if(IsBGRemoved || IsNormalized){
            CreateAndShowSignalPlot();         
            jCbxCurveType.setEnabled(false);
            jBtnConfirmClose.setEnabled(true);
        }
        
    }//GEN-LAST:event_jBtnApplyChangesTempActionPerformed

    private void jBtnConfirmCloseAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jBtnConfirmCloseAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnConfirmCloseAncestorAdded
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnApplyChangesTemp;
    private javax.swing.JButton jBtnCancelClose;
    private javax.swing.JButton jBtnConfirmClose;
    private javax.swing.JButton jBtnReset;
    private javax.swing.JCheckBox jCBToAll;
    private javax.swing.JComboBox jCbxCurveType;
    private javax.swing.JCheckBox jCbxNormalize;
    private javax.swing.JCheckBox jCbxSubtractBG;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPnlControl;
    private javax.swing.JPanel jPnlGraphHolder;
    // End of variables declaration//GEN-END:variables
}
