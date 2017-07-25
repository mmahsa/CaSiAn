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

//import com.apple.eawt.Application;
//import com.apple.mrj.MRJApplicationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import com.apple.eawt.Application;


/**
 *
 * @author mahsa.moein
 */
public class FrmSigAnalysis extends javax.swing.JFrame {
      //private javax.swing.JFileChooser fc;
    //private ChartPanel cpanel;
    private Experiment exp;
    private FSigma_Tav FSigmaTav;
    private Integer signal_Index = -1;
    private SignalPlot signalPlot;
    private PlotPoints ISI_TimePlot;
    private boolean ExperimentIsOpen = false;
    public ArrayList<String> sectionNames;

    private FDefineTreatmentName FDefineTreat;
    private File BrowseDefaultPath = null;
    public File SaveDefaultPath = null;

    JMenuItem nMenuItem = new JMenuItem("New Experiment");
    JMenuItem CMenuItem = new JMenuItem("Close Experiment");
    JMenuItem SMenuItem = new JMenuItem("Save Experiment");
    JMenuItem exMenuItem = new JMenuItem("Export Data and Figures");
    JMenuItem eMenuItem = new JMenuItem("Exit");
    JMenuItem UnDMenuItem = new JMenuItem("Undo Last Delete");
    JMenuItem UnDAllMenuItem = new JMenuItem("Undo All Deletes");
    JMenuItem DMenuItem = new JMenuItem("Delete Signal");
    JMenuItem RemoveBgMenuItem = new JMenuItem("Remove Background");
    JMenuItem PTavSigForExpMenuItem = new JMenuItem("Draw Tav_sigma for whole experiment");
    //JMenuItem PTavSigForOneSignalMenuItem = new JMenuItem("Draw Tav_sigma for current signal");
    JMenuItem AboutMenuItem = new JMenuItem("About CaSiAn");

    private ArrayList<Component> focusList;

    public void SetjLblSelectedTimeValue(Signal sig, double x) {     
     //this.jLblSelectedTimeValue.setText(" " + Utils.DoubleToString(x));
             
    }

    public void SetjLblSelectedSignalValue(Signal sig, double y) {
        
     //this.jLblSelectedSignalValue.setText(" " + Utils.DoubleToString(y));
       
    }

    private void ShowPeaksInformations() {
        Signal sig = exp.signals.get(signal_Index);
        int peak_No = sig.getPeaknumber();
        if(peak_No>0)
        {
            this.ShowAMP_SW(sig);
            PTavSigForExpMenuItem.setEnabled(true);            
            jLblSW.setEnabled(true);
            jLblSWSD.setEnabled(true);
            jLblAMP.setEnabled(true);
            jLblAMP_SD.setEnabled(true);
            jLblSpikeWidth.setEnabled(true);
            jLblSpikeWidthSD.setEnabled(true);
            jLblAmplitude.setEnabled(true);
            jLblAmplitudeSD.setEnabled(true);

            jLblSectionNames3.setEnabled(true);
            jComBSectionNamesP3.setEnabled(true);

            jLblSWSec.setEnabled(true);
            jLblSWSDSec.setEnabled(true);
            jLblAMPSec.setEnabled(true);
            jLblAMP_SDSec.setEnabled(true);
            jLblSpikeWidthSec.setEnabled(true);
            jLblSpikeWidthSDSec.setEnabled(true);
            jLblAmplitudeSec.setEnabled(true);
            jLblAmplitudeSDSec.setEnabled(true);
            if(peak_No>1){
                jPnl_ISITime.setVisible(true);
                jPnl_ISITime.setBorder(BorderFactory.createLineBorder(Color.black));
                
            }
            else // peak_No == 1
            {
                this.EmptyISI_TimePanel();
            }
        }
        else
        {
           deactiveSelectPeakComps();
           EmptyLabelsAMP_SW();
           this.EmptyISI_TimePanel();
        }

    }

    private void deactiveSelectPeakComps() {

        jLblSW.setEnabled(false);
        jLblSWSD.setEnabled(false);
        jLblAMP.setEnabled(false);
        jLblAMP_SD.setEnabled(false);
        jLblSpikeWidth.setEnabled(false);
        jLblSpikeWidthSD.setEnabled(false);
        jLblAmplitude.setEnabled(false);
        jLblAmplitudeSD.setEnabled(false);

        jLblSectionNames3.setEnabled(false);
        jComBSectionNamesP3.setEnabled(false);
        jLblSWSec.setEnabled(false);
        jLblSWSDSec.setEnabled(false);
        jLblAMPSec.setEnabled(false);
        jLblAMP_SDSec.setEnabled(false);
        jLblSpikeWidthSec.setEnabled(false);
        jLblSpikeWidthSDSec.setEnabled(false);
        jLblAmplitudeSec.setEnabled(false);
        jLblAmplitudeSDSec.setEnabled(false);

        //PTavSigForExpMenuItem.setEnabled(false);
        //PTavSigForOneSignalMenuItem.setEnabled(false);

        if (this.FSigmaTav != null) {
            this.FSigmaTav.dispose();
            this.exp.SigmaTavShown = false;
        }
    }

    private void DisablejPnlTreatmentPeak() {
        jBtnSectionFindPeaks.setEnabled(false);
        jCbxApplyToAllPeakSection.setEnabled(false);
        jLblPt.setEnabled(false);
        jTxtSectionPeakThreshold.setEnabled(false);
        jLblAmp.setEnabled(false);
        jLblSection.setEnabled(false);
        jComBSectionNamesP2.setEnabled(false);
        
        deactiveSelectPeakComps();
    }

    private void EnablejPnlTreatmentPeak() {
        jBtnSectionFindPeaks.setEnabled(true);
        jCbxApplyToAllPeakSection.setEnabled(true);
        jLblPt.setEnabled(true);
        jTxtSectionPeakThreshold.setEnabled(true);
        jLblAmp.setEnabled(true);
        jLblSection.setEnabled(true);
       
        jComBSectionNamesP2.setEnabled(true);
//        if (jComBSectionNamesP2.getItemCount() > 0) {
//            jComBSectionNamesP2.setSelectedIndex(0);
//        }

        //activeSelectPeakComps();
    }

    private void MakeVisibleTreatmentPanels() {
        jPnlTreatment.setVisible(true);
        jPnlTreatmentPeak.setVisible(true);
        jPnlSignalInfoWithSection.setVisible(true);
        jPnlSignalInfoWithoutSection.setVisible(false);
        PnlSignalPeak.setVisible(false);

        jTxtSectionPeakThreshold.setText("30");
        jComBSectionNamesP1.removeAllItems();
        jComBSectionNamesP2.removeAllItems();
        jComBSectionNamesP3.removeAllItems();

        if (exp.NumberOfSections > 1 && sectionNames != null) {
            for (int i = 0; i < this.sectionNames.size(); i++) {
                jComBSectionNamesP1.addItem(sectionNames.get(i));

                jComBSectionNamesP2.addItem(sectionNames.get(i));

                jComBSectionNamesP3.addItem(sectionNames.get(i));
            }
        }
        DisablejPnlTreatmentPeak();

        SetFocusArrayList(Utils.frameState.OpenSigSignalWithTreat);
    }

    private void MakeInvisibleTretmentPanels() {
        jPnlTreatment.setVisible(false);
        jPnlTreatmentPeak.setVisible(false);
        PnlSignalPeak.setVisible(true);
        jPnlSignalInfoWithSection.setVisible(false);
        jPnlSignalInfoWithoutSection.setVisible(true);

        SetFocusArrayList(Utils.frameState.OpenSigWithoutTreat);
    }

    private void DisablejPnlTreatment() {
        jLblSectionAssign.setEnabled(false);
        jComBSectionNamesP1.setEnabled(false);
        jLblAsFrom.setEnabled(false);
        jTxtSectionFrom.setText("");
        jTxtSectionFrom.setEnabled(false);
        jLblAsTo.setEnabled(false);
        jTxtSextionTo.setText("");
        jTxtSextionTo.setEnabled(false);
        jCbxSectionApplyToAll.setSelected(false);
        jCbxSectionApplyToAll.setEnabled(false);
        jBtnAssignSection.setEnabled(false);
    }

    private void EnablejPnlTreatment() {
        jLblSectionAssign.setEnabled(true);
        jComBSectionNamesP1.setEnabled(true);
        jLblAsFrom.setEnabled(true);
        jTxtSectionFrom.setText("");
        jTxtSectionFrom.setEnabled(true);
        jLblAsTo.setEnabled(true);
        jTxtSextionTo.setText("");
        jTxtSextionTo.setEnabled(true);
        jCbxSectionApplyToAll.setEnabled(true);
        jBtnAssignSection.setEnabled(true);
        if (jComBSectionNamesP1.getItemCount() > 0) {
            jComBSectionNamesP1.setSelectedIndex(0);
        }
    }

    private void EnablejPnlEditSignal() {
        jLblTimeFrom.setEnabled(true);
        jTxtFrom.setText("");
        jTxtFrom.setEnabled(true);
        jLblTimeTo.setEnabled(true);
        jTxtTo.setText("");
        jTxtTo.setEnabled(true);
        
        jCB_applyToAll_time.setEnabled(true);
        jBtnEditSignalTime.setEnabled(true);
        JbtnSignalEdit_Reset.setEnabled(true);
        //jBtnRemoveBackground.setEnabled(true);
        //jPnlPlot.setVisible(true);        
    }

    private void DisablejPnlEditSignal() {
        jLblTimeFrom.setEnabled(false);
        jTxtFrom.setText("");
        jTxtFrom.setEnabled(false);
        jLblTimeTo.setEnabled(false);
        jTxtTo.setText("");
        jTxtTo.setEnabled(false);
        
        jCB_applyToAll_time.setEnabled(false);
        jCB_applyToAll_time.setSelected(false);
        jBtnEditSignalTime.setEnabled(false);
        JbtnSignalEdit_Reset.setEnabled(false);
        //jBtnRemoveBackground.setEnabled(false);
    }

    private void EnablePnlSignalPeak() {
        jLblPeakThr.setEnabled(true);
        jTxtPeakThr.setEnabled(true);
        jLblmax.setEnabled(true);
        jCB_applyToAllPeak.setEnabled(true);
        jBtnFindPeaks.setEnabled(true);
    }

    private void DisablePnlSignalPeak() {
        jLblPeakThr.setEnabled(false);
        jTxtPeakThr.setEnabled(false);
        jLblmax.setEnabled(false);
        jCB_applyToAllPeak.setEnabled(false);
        jBtnFindPeaks.setEnabled(false);
    }

    private void SetjPnlTreatmentPeak() {
        Signal sig = this.exp.signals.get(signal_Index);
        if (sig.IsTimeAddedToAllSections()) {
            EnablejPnlTreatmentPeak();
           ShowPeaksInformations();
            
        } else {
            DisablejPnlTreatmentPeak();
            deactiveSelectPeakComps();
        }
    }

    private void activeComps_ByOpenSignal() {

        jPnlPlot.setEnabled(true);
        jPnlPlot.setVisible(true);
        jPnl_ISITime.setEnabled(true);
        jPnl_ISITime.setVisible(false);
        // jPnlControl.setVisible(true);    
        jLbl_Id.setVisible(true);
        jLblOf.setVisible(true);
        jLblTotal.setVisible(true);
        jBtnBack.setVisible(true);
        jBtnForward.setVisible(true);
        jBtnLast.setVisible(true);
        jBtnFirst.setVisible(true);
        jBtnDelete.setVisible(true);
        jBtnUndoDel.setVisible(true);
        jBtnFolderBrowse.setEnabled(false);
        jTxt_FileNameContains.setEditable(false);
        jTxt_sig_col_name_contains.setEditable(false);
        //jTx_time_bet_two_sample.setEditable(false);
        jTxt_time_column_no.setEditable(false);
        jTxtTreatment.setEditable(false);
        jTxtFolderName.setEditable(false);
        
        jBtnEditSignalTime.setEnabled(true);
        jLblTimeFrom.setEnabled(true);
        jTxtFrom.setEnabled(true);
        jLblTimeTo.setEnabled(true);
        jTxtTo.setEnabled(true);
        jBtnEditSignalTime.setEnabled(true);
        jLblPeakThr.setEnabled(true);
        jTxtPeakThr.setEnabled(true);
        jTxtPeakThr.setText("30");
        jLblmax.setEnabled(true);
        jBtnFindPeaks.setEnabled(true);
        jCB_applyToAll_time.setEnabled(true);
        jCB_applyToAllPeak.setEnabled(true);
        JbtnSignalEdit_Reset.setEnabled(true);
        //jBtnRemoveBackground.setEnabled(true);

        UnDMenuItem.setEnabled(true);
        UnDAllMenuItem.setEnabled(true);
        DMenuItem.setEnabled(true);
        jPnlPlot.setBorder(BorderFactory.createLineBorder(Color.black));

    }

    private void SetFocusArrayList(Utils.frameState FrameState) {
        if (FrameState == Utils.frameState.FormLoad) {
            this.focusList = null;
            this.focusList = new ArrayList();
            focusList.add(jBtnFolderBrowse);
            focusList.add(jTxt_FileNameContains);
            focusList.add(jTxt_sig_col_name_contains);
            focusList.add(jTxt_time_column_no);
            focusList.add(jTx_time_bet_two_sample);
            focusList.add(jTxtTreatment);
            focusList.add(jBtnOpenSig);
            return;
        }
        if (FrameState == Utils.frameState.OpenSigWithoutTreat) {
            this.focusList = null;
            this.focusList = new ArrayList();
            focusList.add(jTxtFrom);
            focusList.add(jTxtTo);
            
            focusList.add(jCB_applyToAll_time);
            focusList.add(jBtnEditSignalTime);
            
            focusList.add(JbtnSignalEdit_Reset);            
            focusList.add(jTxtPeakThr);
            
            focusList.add(jCB_applyToAllPeak);
            focusList.add(jBtnFindPeaks);
            focusList.add(jBtnForward);
            focusList.add(jBtnBack);
            focusList.add(jBtnFirst);
            focusList.add(jBtnLast);
            focusList.add(jBtnDelete);
            focusList.add(jBtnUndoDel);

        }

        if (FrameState == Utils.frameState.OpenSigSignalWithTreat) {
            this.focusList = null;
            this.focusList = new ArrayList();
            focusList.add(jTxtFrom);
            focusList.add(jTxtTo);
           
            focusList.add(jCB_applyToAll_time);
            focusList.add(jBtnEditSignalTime);
            
            focusList.add(JbtnSignalEdit_Reset);
                        
            focusList.add(jComBSectionNamesP1);
            focusList.add(jTxtSectionFrom);
            focusList.add(jTxtSextionTo);
            focusList.add(jCbxSectionApplyToAll);
            focusList.add(jBtnAssignSection);

            focusList.add(jComBSectionNamesP2);
            focusList.add(jTxtSectionPeakThreshold);
            
            focusList.add(jCbxApplyToAllPeakSection);
            focusList.add(jBtnSectionFindPeaks);
            focusList.add(jComBSectionNamesP3);

            focusList.add(jBtnForward);
            focusList.add(jBtnBack);
            focusList.add(jBtnFirst);
            focusList.add(jBtnLast);
            focusList.add(jBtnDelete);
            focusList.add(jBtnUndoDel);
        }

    }
    
    private void EmptyExperimentComponents(){
        jTxtFolderName.setText("");
        jTxt_FileNameContains.setText("");
        jTxt_sig_col_name_contains.setText("");
        jTx_time_bet_two_sample.setText("1");
        jTxt_time_column_no.setText("1");
        jTxtTreatment.setText("0"); 
    }

    private void deactiveComps_ByframeLoad() {
        
        jPnlPlot.removeAll();
        jPnlPlot.setEnabled(false);
        jPnlPlot.setVisible(false);
        jPnl_ISITime.setEnabled(false);
        //jPnlControl.setVisible(false);
        jLbl_Id.setVisible(false);
        jLblOf.setVisible(false);
        jLblTotal.setVisible(false);
        jBtnBack.setVisible(false);
        jBtnForward.setVisible(false);
        jBtnLast.setVisible(false);
        jBtnFirst.setVisible(false);
        jBtnDelete.setVisible(false);
        jBtnUndoDel.setVisible(false);
        jBtnFolderBrowse.setEnabled(true);
        jTxt_FileNameContains.setEditable(true);
        jTxt_sig_col_name_contains.setEditable(true);
        jTx_time_bet_two_sample.setEditable(true);
        jTxt_time_column_no.setEditable(true);
        jTxtTreatment.setEditable(true);
       
        jBtnOpenSig.setEnabled(true);
        jBtnEditSignalTime.setEnabled(false);
        jLblTimeFrom.setEnabled(false);
        jTxtFrom.setEnabled(false);
        jLblTimeTo.setEnabled(false);
        jTxtTo.setEnabled(false);
        jBtnEditSignalTime.setEnabled(false);
        jLblPeakThr.setEnabled(false);
        jTxtPeakThr.setEnabled(false);
        jLblmax.setEnabled(false);
        jBtnFindPeaks.setEnabled(false);
        jCB_applyToAll_time.setEnabled(false);
        jCB_applyToAllPeak.setEnabled(false);
        JbtnSignalEdit_Reset.setEnabled(false);

        deactiveSelectPeakComps();
        UnDMenuItem.setEnabled(false);
        UnDAllMenuItem.setEnabled(false);
        DMenuItem.setEnabled(false);

        jPnlTreatment.setVisible(false);
        jPnlTreatmentPeak.setVisible(false);

        jPnlSignalInfoWithSection.setVisible(false);
        jPnlSignalInfoWithoutSection.setVisible(true);

        jPnlFake.setVisible(false);
        
        EmptySignalPanel();
        SetFocusArrayList(Utils.frameState.FormLoad);
        EmptyISI_TimePanel();
        
        jTxtFrom.setText("");
        jTxtTo.setText("");
        
        jCB_applyToAll_time.setSelected(false);
        jComBSectionNamesP1.removeAllItems();
        jTxtSectionFrom.setText("");
        jTxtSextionTo.setText("");
        jCbxSectionApplyToAll.setSelected(false);
        jTxtPeakThr.setText("");
        jCB_applyToAllPeak.setSelected(false);
        jComBSectionNamesP2.removeAllItems();
        jTxtSectionPeakThreshold.setText("");
        jCbxApplyToAllPeakSection.setSelected(false);
        jComBSectionNamesP3.removeAllItems();
        
        
        
        this.exp = null;
        this.FSigmaTav = null;
        this.signal_Index = -1;
        this.signalPlot = null;
        this.ISI_TimePlot = null;
        this.sectionNames = null;
        this.FDefineTreat = null;
    
    }

    public FrmSigAnalysis() {
        initComponents();
        ImageIcon img_frame = new ImageIcon(this.getClass().getClassLoader().getResource("resource/CASA.png"));
        //in windows
        //this.setIconImage(img_frame.getImage());
        
        //In mac
//        Application application = Application.getApplication();
//        application.setDockIconImage(img_frame.getImage());
        
        
        setLocationRelativeTo(null);
        SetLayout();

        ImageIcon img_new = new ImageIcon(this.getClass().getClassLoader().getResource("resource/new.png"));
        nMenuItem.setIcon(img_new);

        ImageIcon img_close = new ImageIcon(this.getClass().getClassLoader().getResource("resource/close.png"));
        CMenuItem.setIcon(img_close);

        ImageIcon img_save = new ImageIcon(this.getClass().getClassLoader().getResource("resource/save.png"));
        SMenuItem.setIcon(img_save);

        ImageIcon img_export = new ImageIcon(this.getClass().getClassLoader().getResource("resource/export.png"));
        exMenuItem.setIcon(img_export);
        ImageIcon img_exit = new ImageIcon(this.getClass().getClassLoader().getResource("resource/exit.png"));
        eMenuItem.setIcon(img_exit);
        
        ImageIcon img_Waves = new ImageIcon(this.getClass().getClassLoader().getResource("resource/waves.png"));
        RemoveBgMenuItem.setIcon(img_Waves);

        ImageIcon img_bro = new ImageIcon(this.getClass().getClassLoader().getResource("resource/browse.png"));
        jBtnFolderBrowse.setIcon(img_bro);

        ImageIcon img_open = new ImageIcon(this.getClass().getClassLoader().getResource("resource/open.png"));
        jBtnOpenSig.setIcon(img_open);

        ImageIcon img_Edit = new ImageIcon(this.getClass().getClassLoader().getResource("resource/edit.png"));
        jBtnEditSignalTime.setIcon(img_Edit);

        ImageIcon img_Reset = new ImageIcon(this.getClass().getClassLoader().getResource("resource/reset.png"));
        JbtnSignalEdit_Reset.setIcon(img_Reset);    
        
        ImageIcon img_Find = new ImageIcon(this.getClass().getClassLoader().getResource("resource/Find.png"));
        jBtnFindPeaks.setIcon(img_Find);
        jBtnSectionFindPeaks.setIcon(img_Find);

        ImageIcon img_assign = new ImageIcon(this.getClass().getClassLoader().getResource("resource/Assign.png"));
        jBtnAssignSection.setIcon(img_assign);

        ImageIcon img_delete = new ImageIcon(this.getClass().getClassLoader().getResource("resource/delete.png"));
        DMenuItem.setIcon(img_delete);
        jBtnDelete.setIcon(img_delete);

        ImageIcon img_undo = new ImageIcon(this.getClass().getClassLoader().getResource("resource/Undo.png"));
        UnDMenuItem.setIcon(img_undo);
        jBtnUndoDel.setIcon(img_undo);

        ImageIcon img_undoAll = new ImageIcon(this.getClass().getClassLoader().getResource("resource/UndoAll.png"));
        UnDAllMenuItem.setIcon(img_undoAll);

        ImageIcon img_expPlot = new ImageIcon(this.getClass().getClassLoader().getResource("resource/expPlot.png"));
        PTavSigForExpMenuItem.setIcon(img_expPlot);
        
        

        jPnl_ISITime.setLayout(new BorderLayout());

        deactiveComps_ByframeLoad();
        EmptyExperimentComponents();
        this.setFocusTraversalPolicy(new MyFocusTraversalPolicy());

        createMenuBar();
        SetKeyboardShortCuts();
        this.getContentPane().setBackground(Color.lightGray);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                DisposeForm();
            }
        });

        //this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
    }

    private void SetKeyboardShortCuts() {
        InputMap inputMapF = jBtnForward.getInputMap(JButton.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke forward = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        inputMapF.put(forward, "RIGHT");
        jBtnForward.getActionMap().put("RIGHT", new ClickAction(jBtnForward));

        InputMap inputMapB = jBtnBack.getInputMap(JButton.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke back = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        inputMapB.put(back, "LEFT");
        jBtnBack.getActionMap().put("LEFT", new ClickAction(jBtnBack));

        InputMap inputMapDelete = jBtnDelete.getInputMap(JButton.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
        inputMapDelete.put(delete, "DELETE");
        jBtnDelete.getActionMap().put("DELETE", new ClickAction(jBtnDelete));

        InputMap inputMapDelete2 = jBtnDelete.getInputMap(JButton.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke delete2 = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        inputMapDelete2.put(delete2, "DELETE");
        jBtnDelete.getActionMap().put("DELETE", new ClickAction(jBtnDelete));

        InputMap inputMapUndoDelete = jBtnUndoDel.getInputMap(JButton.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke undodelete = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0);
        inputMapUndoDelete.put(undodelete, "UNDODELETE");
        jBtnUndoDel.getActionMap().put("UNDODELETE", new ClickAction(jBtnUndoDel));
    }

    public class ClickAction extends AbstractAction {

        private final JButton button;

        public ClickAction(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.doClick();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopMenu_signalPlot = new javax.swing.JPopupMenu();
        jPnlTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtFolderName = new javax.swing.JTextField();
        jBtnFolderBrowse = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTxt_FileNameContains = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTx_time_bet_two_sample = new javax.swing.JTextField();
        jBtnOpenSig = new javax.swing.JButton();
        jTxt_time_column_no = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTxt_sig_col_name_contains = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTxtTreatment = new javax.swing.JTextField();
        jPnlPlotHolders = new javax.swing.JPanel();
        jPnl_ISITime = new javax.swing.JPanel();
        jPnlPlot = new javax.swing.JPanel();
        jPnlControl = new javax.swing.JPanel();
        jPnlButtons = new javax.swing.JPanel();
        jLblOf = new javax.swing.JLabel();
        jLbl_Id = new javax.swing.JLabel();
        jLblTotal = new javax.swing.JLabel();
        jBtnBack = new javax.swing.JButton();
        jBtnFirst = new javax.swing.JButton();
        jBtnUndoDel = new javax.swing.JButton();
        jBtnForward = new javax.swing.JButton();
        jBtnLast = new javax.swing.JButton();
        jBtnDelete = new javax.swing.JButton();
        jPnlFunction = new javax.swing.JPanel();
        jPnlEditSignal = new javax.swing.JPanel();
        jLblTimeFrom = new javax.swing.JLabel();
        jTxtFrom = new javax.swing.JTextField();
        jLblTimeTo = new javax.swing.JLabel();
        jTxtTo = new javax.swing.JTextField();
        jBtnEditSignalTime = new javax.swing.JButton();
        jCB_applyToAll_time = new javax.swing.JCheckBox();
        JbtnSignalEdit_Reset = new javax.swing.JButton();
        jPnlTreatment = new javax.swing.JPanel();
        jLblAsFrom = new javax.swing.JLabel();
        jTxtSectionFrom = new javax.swing.JTextField();
        jLblAsTo = new javax.swing.JLabel();
        jTxtSextionTo = new javax.swing.JTextField();
        jBtnAssignSection = new javax.swing.JButton();
        jCbxSectionApplyToAll = new javax.swing.JCheckBox();
        jLblSectionAssign = new javax.swing.JLabel();
        jComBSectionNamesP1 = new javax.swing.JComboBox();
        PnlSignalPeak = new javax.swing.JPanel();
        jLblPeakThr = new javax.swing.JLabel();
        jTxtPeakThr = new javax.swing.JTextField();
        jLblmax = new javax.swing.JLabel();
        jBtnFindPeaks = new javax.swing.JButton();
        jCB_applyToAllPeak = new javax.swing.JCheckBox();
        jPnlTreatmentPeak = new javax.swing.JPanel();
        jLblSection = new javax.swing.JLabel();
        jComBSectionNamesP2 = new javax.swing.JComboBox();
        jLblPt = new javax.swing.JLabel();
        jTxtSectionPeakThreshold = new javax.swing.JTextField();
        jLblAmp = new javax.swing.JLabel();
        jCbxApplyToAllPeakSection = new javax.swing.JCheckBox();
        jBtnSectionFindPeaks = new javax.swing.JButton();
        jPnlSignalInfoWithoutSection = new javax.swing.JPanel();
        jLblSW = new javax.swing.JLabel();
        jLblSWSD = new javax.swing.JLabel();
        jLblAMP = new javax.swing.JLabel();
        jLblAMP_SD = new javax.swing.JLabel();
        jLblSpikeWidth = new javax.swing.JLabel();
        jLblSpikeWidthSD = new javax.swing.JLabel();
        jLblAmplitude = new javax.swing.JLabel();
        jLblAmplitudeSD = new javax.swing.JLabel();
        jPnlSignalInfoWithSection = new javax.swing.JPanel();
        jLblSWSec = new javax.swing.JLabel();
        jLblSWSDSec = new javax.swing.JLabel();
        jLblAMPSec = new javax.swing.JLabel();
        jLblAMP_SDSec = new javax.swing.JLabel();
        jLblSectionNames3 = new javax.swing.JLabel();
        jComBSectionNamesP3 = new javax.swing.JComboBox();
        jLblSpikeWidthSec = new javax.swing.JLabel();
        jLblSpikeWidthSDSec = new javax.swing.JLabel();
        jLblAmplitudeSec = new javax.swing.JLabel();
        jLblAmplitudeSDSec = new javax.swing.JLabel();
        jPnlFake = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CaSiAn");
        setBounds(new java.awt.Rectangle(0, 22, 1075, 720));
        setForeground(new java.awt.Color(255, 204, 204));
        setMinimumSize(new java.awt.Dimension(1100, 720));
        setName("FrmSignalAnalysis"); // NOI18N
        setSize(new java.awt.Dimension(1075, 730));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlTop.setPreferredSize(new java.awt.Dimension(1070, 116));
        jPnlTop.setSize(new java.awt.Dimension(1070, 116));

        jLabel1.setText("Signal Folder path:");

        jTxtFolderName.setPreferredSize(new java.awt.Dimension(370, 28));
        jTxtFolderName.setRequestFocusEnabled(false);

        jBtnFolderBrowse.setText("Browse");
        jBtnFolderBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFolderBrowseActionPerformed(evt);
            }
        });

        jLabel5.setText("Signal file name contains:");

        jTxt_FileNameContains.setPreferredSize(new java.awt.Dimension(98, 28));

        jLabel4.setText("Time between two image (s):");

        jTx_time_bet_two_sample.setPreferredSize(new java.awt.Dimension(98, 28));
        jTx_time_bet_two_sample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTx_time_bet_two_sampleActionPerformed(evt);
            }
        });

        jBtnOpenSig.setText("Open Experiment");
        jBtnOpenSig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnOpenSigActionPerformed(evt);
            }
        });

        jTxt_time_column_no.setPreferredSize(new java.awt.Dimension(58, 28));

        jLabel6.setText("Time column number:");

        jTxt_sig_col_name_contains.setPreferredSize(new java.awt.Dimension(72, 28));

        jLabel3.setText("Signal column name contains:");

        jLabel2.setText("Number of treatments:");

        jTxtTreatment.setPreferredSize(new java.awt.Dimension(72, 28));
        jTxtTreatment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtTreatmentFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPnlTopLayout = new javax.swing.GroupLayout(jPnlTop);
        jPnlTop.setLayout(jPnlTopLayout);
        jPnlTopLayout.setHorizontalGroup(
            jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPnlTopLayout.createSequentialGroup()
                        .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(6, 6, 6)
                        .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTxt_FileNameContains, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTx_time_bet_two_sample, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)))
                    .addGroup(jPnlTopLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtFolderName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlTopLayout.createSequentialGroup()
                        .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtTreatment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxt_sig_col_name_contains, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlTopLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTxt_time_column_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlTopLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnOpenSig, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jBtnFolderBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(269, Short.MAX_VALUE))
        );
        jPnlTopLayout.setVerticalGroup(
            jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnFolderBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTxtFolderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTxt_FileNameContains, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTxt_sig_col_name_contains, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTxt_time_column_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnOpenSig)
                    .addComponent(jTx_time_bet_two_sample, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jTxtTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPnlPlotHolders.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlPlotHolders.setMinimumSize(new java.awt.Dimension(750, 610));
        jPnlPlotHolders.setPreferredSize(new java.awt.Dimension(750, 610));
        jPnlPlotHolders.setSize(new java.awt.Dimension(750, 610));

        jPnl_ISITime.setMaximumSize(new java.awt.Dimension(32767, 500));
        jPnl_ISITime.setMinimumSize(new java.awt.Dimension(740, 220));
        jPnl_ISITime.setPreferredSize(new java.awt.Dimension(740, 220));
        jPnl_ISITime.setSize(new java.awt.Dimension(740, 220));

        javax.swing.GroupLayout jPnl_ISITimeLayout = new javax.swing.GroupLayout(jPnl_ISITime);
        jPnl_ISITime.setLayout(jPnl_ISITimeLayout);
        jPnl_ISITimeLayout.setHorizontalGroup(
            jPnl_ISITimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );
        jPnl_ISITimeLayout.setVerticalGroup(
            jPnl_ISITimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPnlPlot.setForeground(new java.awt.Color(204, 204, 204));
        jPnlPlot.setMinimumSize(new java.awt.Dimension(740, 290));
        jPnlPlot.setPreferredSize(new java.awt.Dimension(740, 290));
        jPnlPlot.setRequestFocusEnabled(false);
        jPnlPlot.setSize(new java.awt.Dimension(740, 290));

        javax.swing.GroupLayout jPnlPlotLayout = new javax.swing.GroupLayout(jPnlPlot);
        jPnlPlot.setLayout(jPnlPlotLayout);
        jPnlPlotLayout.setHorizontalGroup(
            jPnlPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );
        jPnlPlotLayout.setVerticalGroup(
            jPnlPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        jPnlControl.setPreferredSize(new java.awt.Dimension(740, 86));
        jPnlControl.setRequestFocusEnabled(false);
        jPnlControl.setSize(new java.awt.Dimension(740, 86));

        jPnlButtons.setPreferredSize(new java.awt.Dimension(730, 70));
        jPnlButtons.setSize(new java.awt.Dimension(730, 70));

        jLblOf.setBackground(new java.awt.Color(102, 0, 102));
        jLblOf.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLblOf.setText("of");

        jLbl_Id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLbl_Id.setForeground(new java.awt.Color(102, 0, 51));
        jLbl_Id.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_Id.setToolTipText("");
        jLbl_Id.setAutoscrolls(true);
        jLbl_Id.setPreferredSize(new java.awt.Dimension(35, 15));
        jLbl_Id.setSize(new java.awt.Dimension(35, 15));

        jLblTotal.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLblTotal.setForeground(new java.awt.Color(102, 0, 51));
        jLblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLblTotal.setToolTipText("");
        jLblTotal.setPreferredSize(new java.awt.Dimension(35, 15));
        jLblTotal.setRequestFocusEnabled(false);
        jLblTotal.setSize(new java.awt.Dimension(35, 15));

        jBtnBack.setBackground(new java.awt.Color(204, 204, 204));
        jBtnBack.setText("< Previous");
        jBtnBack.setPreferredSize(new java.awt.Dimension(100, 29));
        jBtnBack.setSize(new java.awt.Dimension(100, 30));
        jBtnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBackActionPerformed(evt);
            }
        });
        jBtnBack.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnBackKeyPressed(evt);
            }
        });

        jBtnFirst.setText("<< First");
        jBtnFirst.setPreferredSize(new java.awt.Dimension(100, 29));
        jBtnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFirstActionPerformed(evt);
            }
        });
        jBtnFirst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnFirstKeyPressed(evt);
            }
        });

        jBtnUndoDel.setText("Undo delete");
        jBtnUndoDel.setPreferredSize(new java.awt.Dimension(130, 29));
        jBtnUndoDel.setSize(new java.awt.Dimension(130, 29));
        jBtnUndoDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUndoDelActionPerformed(evt);
            }
        });
        jBtnUndoDel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnUndoDelKeyPressed(evt);
            }
        });

        jBtnForward.setBackground(new java.awt.Color(204, 204, 204));
        jBtnForward.setText("Next >");
        jBtnForward.setPreferredSize(new java.awt.Dimension(100, 29));
        jBtnForward.setSize(new java.awt.Dimension(100, 29));
        jBtnForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnForwardActionPerformed(evt);
            }
        });
        jBtnForward.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnForwardKeyPressed(evt);
            }
        });

        jBtnLast.setText("Last >>");
        jBtnLast.setPreferredSize(new java.awt.Dimension(100, 29));
        jBtnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLastActionPerformed(evt);
            }
        });
        jBtnLast.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnLastKeyPressed(evt);
            }
        });

        jBtnDelete.setText("Delete signal");
        jBtnDelete.setPreferredSize(new java.awt.Dimension(130, 29));
        jBtnDelete.setSize(new java.awt.Dimension(130, 29));
        jBtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDeleteActionPerformed(evt);
            }
        });
        jBtnDelete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnDeleteKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPnlButtonsLayout = new javax.swing.GroupLayout(jPnlButtons);
        jPnlButtons.setLayout(jPnlButtonsLayout);
        jPnlButtonsLayout.setHorizontalGroup(
            jPnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlButtonsLayout.createSequentialGroup()
                .addGroup(jPnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlButtonsLayout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(jLbl_Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLblOf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlButtonsLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jBtnUndoDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPnlButtonsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jBtnDelete, jBtnUndoDel});

        jPnlButtonsLayout.setVerticalGroup(
            jPnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLbl_Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblOf)
                    .addComponent(jLblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jBtnUndoDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPnlControlLayout = new javax.swing.GroupLayout(jPnlControl);
        jPnlControl.setLayout(jPnlControlLayout);
        jPnlControlLayout.setHorizontalGroup(
            jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlControlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPnlControlLayout.setVerticalGroup(
            jPnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPnlPlotHoldersLayout = new javax.swing.GroupLayout(jPnlPlotHolders);
        jPnlPlotHolders.setLayout(jPnlPlotHoldersLayout);
        jPnlPlotHoldersLayout.setHorizontalGroup(
            jPnlPlotHoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotHoldersLayout.createSequentialGroup()
                .addGroup(jPnlPlotHoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPnlPlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnl_ISITime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        jPnlPlotHoldersLayout.setVerticalGroup(
            jPnlPlotHoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPlotHoldersLayout.createSequentialGroup()
                .addComponent(jPnlPlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPnl_ISITime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPnlControl, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        jPnlFunction.setBackground(new java.awt.Color(0, 51, 102));
        jPnlFunction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPnlFunction.setMinimumSize(new java.awt.Dimension(315, 645));
        jPnlFunction.setPreferredSize(new java.awt.Dimension(315, 572));
        jPnlFunction.setSize(new java.awt.Dimension(315, 572));

        jPnlEditSignal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlEditSignal.setPreferredSize(new java.awt.Dimension(305, 80));
        jPnlEditSignal.setRequestFocusEnabled(false);
        jPnlEditSignal.setSize(new java.awt.Dimension(305, 80));

        jLblTimeFrom.setText("From:");

        jTxtFrom.setPreferredSize(new java.awt.Dimension(70, 28));
        jTxtFrom.setSize(new java.awt.Dimension(70, 28));

        jLblTimeTo.setText("To:");

        jTxtTo.setPreferredSize(new java.awt.Dimension(80, 28));
        jTxtTo.setSize(new java.awt.Dimension(80, 28));

        jBtnEditSignalTime.setText("Edit");
        jBtnEditSignalTime.setToolTipText("Edit Signal");
        jBtnEditSignalTime.setPreferredSize(new java.awt.Dimension(90, 29));
        jBtnEditSignalTime.setSize(new java.awt.Dimension(90, 29));
        jBtnEditSignalTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditSignalTimeActionPerformed(evt);
            }
        });
        jBtnEditSignalTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnEditSignalTimeKeyPressed(evt);
            }
        });

        jCB_applyToAll_time.setText("apply to all");
        jCB_applyToAll_time.setPreferredSize(new java.awt.Dimension(85, 23));
        jCB_applyToAll_time.setSize(new java.awt.Dimension(85, 0));
        jCB_applyToAll_time.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCB_applyToAll_timeKeyPressed(evt);
            }
        });

        JbtnSignalEdit_Reset.setText("Reset");
        JbtnSignalEdit_Reset.setToolTipText("Reset Signal");
        JbtnSignalEdit_Reset.setPreferredSize(new java.awt.Dimension(90, 29));
        JbtnSignalEdit_Reset.setSize(new java.awt.Dimension(90, 29));
        JbtnSignalEdit_Reset.setVerifyInputWhenFocusTarget(false);
        JbtnSignalEdit_Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnSignalEdit_ResetActionPerformed(evt);
            }
        });
        JbtnSignalEdit_Reset.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JbtnSignalEdit_ResetKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPnlEditSignalLayout = new javax.swing.GroupLayout(jPnlEditSignal);
        jPnlEditSignal.setLayout(jPnlEditSignalLayout);
        jPnlEditSignalLayout.setHorizontalGroup(
            jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlEditSignalLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlEditSignalLayout.createSequentialGroup()
                        .addComponent(jLblTimeFrom)
                        .addGap(3, 3, 3)
                        .addComponent(jTxtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLblTimeTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlEditSignalLayout.createSequentialGroup()
                        .addComponent(jCB_applyToAll_time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jBtnEditSignalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(JbtnSignalEdit_Reset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPnlEditSignalLayout.setVerticalGroup(
            jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlEditSignalLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblTimeFrom)
                    .addComponent(jTxtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblTimeTo)
                    .addComponent(jTxtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnEditSignalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPnlEditSignalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCB_applyToAll_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JbtnSignalEdit_Reset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPnlTreatment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlTreatment.setMinimumSize(new java.awt.Dimension(305, 110));
        jPnlTreatment.setPreferredSize(new java.awt.Dimension(305, 110));
        jPnlTreatment.setSize(new java.awt.Dimension(305, 110));

        jLblAsFrom.setText("From:");

        jTxtSectionFrom.setPreferredSize(new java.awt.Dimension(80, 28));
        jTxtSectionFrom.setSize(new java.awt.Dimension(80, 28));

        jLblAsTo.setText("To:");

        jTxtSextionTo.setPreferredSize(new java.awt.Dimension(80, 28));
        jTxtSextionTo.setSize(new java.awt.Dimension(80, 28));

        jBtnAssignSection.setText("Assign time");
        jBtnAssignSection.setPreferredSize(new java.awt.Dimension(115, 29));
        jBtnAssignSection.setSize(new java.awt.Dimension(115, 29));
        jBtnAssignSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAssignSectionActionPerformed(evt);
            }
        });
        jBtnAssignSection.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnAssignSectionKeyPressed(evt);
            }
        });

        jCbxSectionApplyToAll.setText("apply to all");
        jCbxSectionApplyToAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCbxSectionApplyToAllKeyPressed(evt);
            }
        });

        jLblSectionAssign.setText("Section:");

        jComBSectionNamesP1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", " ", " " }));
        jComBSectionNamesP1.setPreferredSize(new java.awt.Dimension(206, 27));
        jComBSectionNamesP1.setSize(new java.awt.Dimension(206, 27));

        javax.swing.GroupLayout jPnlTreatmentLayout = new javax.swing.GroupLayout(jPnlTreatment);
        jPnlTreatment.setLayout(jPnlTreatmentLayout);
        jPnlTreatmentLayout.setHorizontalGroup(
            jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTreatmentLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlTreatmentLayout.createSequentialGroup()
                        .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlTreatmentLayout.createSequentialGroup()
                                .addComponent(jLblAsFrom)
                                .addGap(18, 18, 18)
                                .addComponent(jTxtSectionFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCbxSectionApplyToAll))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLblAsTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtSextionTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnAssignSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPnlTreatmentLayout.createSequentialGroup()
                        .addComponent(jLblSectionAssign)
                        .addGap(4, 4, 4)
                        .addComponent(jComBSectionNamesP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPnlTreatmentLayout.setVerticalGroup(
            jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTreatmentLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblSectionAssign)
                    .addComponent(jComBSectionNamesP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblAsFrom)
                    .addComponent(jTxtSectionFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAsTo)
                    .addComponent(jTxtSextionTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPnlTreatmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCbxSectionApplyToAll)
                    .addComponent(jBtnAssignSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        PnlSignalPeak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PnlSignalPeak.setPreferredSize(new java.awt.Dimension(305, 80));
        PnlSignalPeak.setSize(new java.awt.Dimension(305, 80));

        jLblPeakThr.setText("Peak threshold:");

        jTxtPeakThr.setPreferredSize(new java.awt.Dimension(54, 28));

        jLblmax.setText("% of max amplitude");

        jBtnFindPeaks.setText("Find");
        jBtnFindPeaks.setPreferredSize(new java.awt.Dimension(115, 29));
        jBtnFindPeaks.setSize(new java.awt.Dimension(115, 29));
        jBtnFindPeaks.setVerifyInputWhenFocusTarget(false);
        jBtnFindPeaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFindPeaksActionPerformed(evt);
            }
        });
        jBtnFindPeaks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnFindPeaksKeyPressed(evt);
            }
        });

        jCB_applyToAllPeak.setText("apply to all");
        jCB_applyToAllPeak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCB_applyToAllPeakKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout PnlSignalPeakLayout = new javax.swing.GroupLayout(PnlSignalPeak);
        PnlSignalPeak.setLayout(PnlSignalPeakLayout);
        PnlSignalPeakLayout.setHorizontalGroup(
            PnlSignalPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(PnlSignalPeakLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlSignalPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(PnlSignalPeakLayout.createSequentialGroup()
                        .addComponent(jLblPeakThr)
                        .addGap(3, 3, 3)
                        .addComponent(jTxtPeakThr, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLblmax))
                    .addGroup(PnlSignalPeakLayout.createSequentialGroup()
                        .addComponent(jCB_applyToAllPeak)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtnFindPeaks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        PnlSignalPeakLayout.setVerticalGroup(
            PnlSignalPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSignalPeakLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlSignalPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblPeakThr)
                    .addComponent(jTxtPeakThr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblmax))
                .addGap(4, 4, 4)
                .addGroup(PnlSignalPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnFindPeaks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCB_applyToAllPeak))
                .addContainerGap())
        );

        jPnlTreatmentPeak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlTreatmentPeak.setMinimumSize(new java.awt.Dimension(305, 110));
        jPnlTreatmentPeak.setPreferredSize(new java.awt.Dimension(305, 110));
        jPnlTreatmentPeak.setSize(new java.awt.Dimension(305, 110));

        jLblSection.setText("Section:");

        jComBSectionNamesP2.setPreferredSize(new java.awt.Dimension(206, 27));
        jComBSectionNamesP2.setSize(new java.awt.Dimension(206, 27));

        jLblPt.setText("Peak threshold:");

        jTxtSectionPeakThreshold.setPreferredSize(new java.awt.Dimension(54, 28));

        jLblAmp.setText("% of max amplitude");

        jCbxApplyToAllPeakSection.setText("apply to all");
        jCbxApplyToAllPeakSection.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCbxApplyToAllPeakSectionKeyPressed(evt);
            }
        });

        jBtnSectionFindPeaks.setText("Find");
        jBtnSectionFindPeaks.setPreferredSize(new java.awt.Dimension(115, 29));
        jBtnSectionFindPeaks.setSize(new java.awt.Dimension(115, 29));
        jBtnSectionFindPeaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSectionFindPeaksActionPerformed(evt);
            }
        });
        jBtnSectionFindPeaks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtnSectionFindPeaksKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPnlTreatmentPeakLayout = new javax.swing.GroupLayout(jPnlTreatmentPeak);
        jPnlTreatmentPeak.setLayout(jPnlTreatmentPeakLayout);
        jPnlTreatmentPeakLayout.setHorizontalGroup(
            jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                        .addComponent(jLblSection)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComBSectionNamesP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                        .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                                .addComponent(jLblPt)
                                .addGap(3, 3, 3)
                                .addComponent(jTxtSectionPeakThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCbxApplyToAllPeakSection))
                        .addGap(3, 3, 3)
                        .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jBtnSectionFindPeaks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLblAmp))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPnlTreatmentPeakLayout.setVerticalGroup(
            jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTreatmentPeakLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblSection)
                    .addComponent(jComBSectionNamesP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblPt)
                    .addComponent(jTxtSectionPeakThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAmp))
                .addGap(4, 4, 4)
                .addGroup(jPnlTreatmentPeakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnSectionFindPeaks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCbxApplyToAllPeakSection))
                .addGap(30, 30, 30))
        );

        jPnlSignalInfoWithoutSection.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlSignalInfoWithoutSection.setPreferredSize(new java.awt.Dimension(305, 70));
        jPnlSignalInfoWithoutSection.setSize(new java.awt.Dimension(305, 70));

        jLblSW.setText("SW:");

        jLblSWSD.setText("SW SD:");

        jLblAMP.setText("AMP:");

        jLblAMP_SD.setText("AMP SD:");

        jLblSpikeWidth.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblSpikeWidth.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblSpikeWidth.setSize(new java.awt.Dimension(75, 200));

        jLblSpikeWidthSD.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblSpikeWidthSD.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblSpikeWidthSD.setSize(new java.awt.Dimension(75, 22));

        jLblAmplitude.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblAmplitude.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblAmplitude.setSize(new java.awt.Dimension(75, 22));

        jLblAmplitudeSD.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblAmplitudeSD.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblAmplitudeSD.setSize(new java.awt.Dimension(75, 22));

        javax.swing.GroupLayout jPnlSignalInfoWithoutSectionLayout = new javax.swing.GroupLayout(jPnlSignalInfoWithoutSection);
        jPnlSignalInfoWithoutSection.setLayout(jPnlSignalInfoWithoutSectionLayout);
        jPnlSignalInfoWithoutSectionLayout.setHorizontalGroup(
            jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlSignalInfoWithoutSectionLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblSW)
                    .addComponent(jLblAMP))
                .addGap(4, 4, 4)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblSpikeWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAmplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblSWSD)
                    .addComponent(jLblAMP_SD))
                .addGap(4, 4, 4)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblSpikeWidthSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAmplitudeSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPnlSignalInfoWithoutSectionLayout.setVerticalGroup(
            jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlSignalInfoWithoutSectionLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblSW)
                    .addComponent(jLblSpikeWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblSWSD)
                    .addComponent(jLblSpikeWidthSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPnlSignalInfoWithoutSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblAMP)
                    .addComponent(jLblAmplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAMP_SD, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAmplitudeSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPnlSignalInfoWithSection.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPnlSignalInfoWithSection.setPreferredSize(new java.awt.Dimension(305, 100));
        jPnlSignalInfoWithSection.setSize(new java.awt.Dimension(305, 100));

        jLblSWSec.setText("SW:");

        jLblSWSDSec.setText("SW SD:");

        jLblAMPSec.setText("AMP:");

        jLblAMP_SDSec.setText("AMP SD:");

        jLblSectionNames3.setText("Section:");

        jComBSectionNamesP3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", " ", " " }));
        jComBSectionNamesP3.setPreferredSize(new java.awt.Dimension(190, 27));
        jComBSectionNamesP3.setSize(new java.awt.Dimension(190, 27));
        jComBSectionNamesP3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComBSectionNamesP3ItemStateChanged(evt);
            }
        });

        jLblSpikeWidthSec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblSpikeWidthSec.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblSpikeWidthSec.setSize(new java.awt.Dimension(75, 200));

        jLblSpikeWidthSDSec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblSpikeWidthSDSec.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblSpikeWidthSDSec.setSize(new java.awt.Dimension(75, 22));

        jLblAmplitudeSec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblAmplitudeSec.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblAmplitudeSec.setSize(new java.awt.Dimension(75, 22));

        jLblAmplitudeSDSec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLblAmplitudeSDSec.setPreferredSize(new java.awt.Dimension(75, 22));
        jLblAmplitudeSDSec.setSize(new java.awt.Dimension(75, 22));

        javax.swing.GroupLayout jPnlSignalInfoWithSectionLayout = new javax.swing.GroupLayout(jPnlSignalInfoWithSection);
        jPnlSignalInfoWithSection.setLayout(jPnlSignalInfoWithSectionLayout);
        jPnlSignalInfoWithSectionLayout.setHorizontalGroup(
            jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlSignalInfoWithSectionLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblSectionNames3)
                    .addComponent(jLblSWSec)
                    .addComponent(jLblAMPSec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlSignalInfoWithSectionLayout.createSequentialGroup()
                        .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblSpikeWidthSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLblAmplitudeSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblSWSDSec)
                            .addComponent(jLblAMP_SDSec))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblSpikeWidthSDSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLblAmplitudeSDSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jComBSectionNamesP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPnlSignalInfoWithSectionLayout.setVerticalGroup(
            jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlSignalInfoWithSectionLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblSectionNames3)
                    .addComponent(jComBSectionNamesP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblSWSec)
                    .addComponent(jLblSpikeWidthSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblSWSDSec)
                    .addComponent(jLblSpikeWidthSDSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPnlSignalInfoWithSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLblAMPSec)
                    .addComponent(jLblAmplitudeSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAMP_SDSec, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblAmplitudeSDSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout jPnlFunctionLayout = new javax.swing.GroupLayout(jPnlFunction);
        jPnlFunction.setLayout(jPnlFunctionLayout);
        jPnlFunctionLayout.setHorizontalGroup(
            jPnlFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlFunctionLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPnlFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPnlEditSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnlSignalPeak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlTreatmentPeak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlSignalInfoWithoutSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlSignalInfoWithSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPnlFunctionLayout.setVerticalGroup(
            jPnlFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlFunctionLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPnlEditSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPnlTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(PnlSignalPeak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPnlTreatmentPeak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPnlSignalInfoWithoutSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPnlSignalInfoWithSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPnlFake.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPnlFake.setPreferredSize(new java.awt.Dimension(305, 3));
        jPnlFake.setSize(new java.awt.Dimension(305, 3));

        javax.swing.GroupLayout jPnlFakeLayout = new javax.swing.GroupLayout(jPnlFake);
        jPnlFake.setLayout(jPnlFakeLayout);
        jPnlFakeLayout.setHorizontalGroup(
            jPnlFakeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        jPnlFakeLayout.setVerticalGroup(
            jPnlFakeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPnlPlotHolders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPnlFake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPnlFunction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnlPlotHolders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnlFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPnlFake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPnlPlotHolders.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnFolderBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFolderBrowseActionPerformed
        // TODO add your handling code here:
        //try{        
        javax.swing.JFileChooser fc = new JFileChooser();
        //fc.setAcceptAllFileFilterUsed(false);
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (BrowseDefaultPath != null) {
            fc.setCurrentDirectory(BrowseDefaultPath);
        }       
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filePath ="";
            if(!file.isDirectory())
            {
                filePath = file.getParent();
            }
            else
            {
               filePath = file.getAbsolutePath() ;
            }

            if (!Utils.CheckFolder(filePath)) {
                JOptionPane.showMessageDialog(this, "Selected path is not a valid path. "
                        + "Selected path does not contain files with readable extension "
                );

            } else {
                jTxtFolderName.setText(filePath);
            }

        }
        fc = null;
    }//GEN-LAST:event_jBtnFolderBrowseActionPerformed

    private void jBtnOpenSigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnOpenSigActionPerformed
        // TODO add your handling code here:        
        boolean fieldOK = true;

        try {
            if (!"".equals(jTx_time_bet_two_sample.getText())) {
                Float.parseFloat(String.valueOf(jTx_time_bet_two_sample.getText()));
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(this, "Please enter a number in the 'Time Between Two Image' field");

            jTx_time_bet_two_sample.setText("1");
            jTx_time_bet_two_sample.requestFocus();
        }

        try {
            if (!"".equals(jTxt_time_column_no.getText())) {
                Integer.parseInt(String.valueOf(jTxt_time_column_no.getText()));
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(this, "Please enter an integer number in the 'Time column number' field");

            jTxt_time_column_no.setText("1");
            jTxt_time_column_no.requestFocus();
        }

        try {
            if (!"".equals(jTxtTreatment.getText())) {
                Integer.parseInt(String.valueOf(jTxtTreatment.getText()));
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(this, "Please enter an integer number in the 'Treatment number' field");

            jTxtTreatment.setText("0");
            jTxtTreatment.requestFocus();
        }
        if (this.sectionNames != null) {
            if (!"0".equals(jTxtTreatment.getText()) && (this.sectionNames.size() != (Integer.parseInt(String.valueOf(jTxtTreatment.getText())) + 1))) {

                JOptionPane.showMessageDialog(this, "The number of section names should be one more than the number of treatments.");
                return;

            }
            if (this.FDefineTreat != null && (this.sectionNames.size() == (Integer.parseInt(String.valueOf(jTxtTreatment.getText())) + 1))) {
                this.FDefineTreat.dispose();
                this.FDefineTreat = null;
            }
        }

        if ("".equals(jTxt_time_column_no.getText())) {
            fieldOK = false;
        }

        if ("".equals(jTxtFolderName.getText())) {
            fieldOK = false;
        }

        if ("".equals(jTx_time_bet_two_sample.getText())) {
            fieldOK = false;
        }

        if ("".equals(jTxt_FileNameContains.getText())) {
            fieldOK = false;
        }

        if ("".equals(jTxt_sig_col_name_contains.getText())) {
            fieldOK = false;
        }

        if (!fieldOK) {
            JOptionPane.showMessageDialog(this, "Please fill the fields correctly");
            return;
        }

        exp = new Experiment(this);        
        exp.setPath_of_signals(jTxtFolderName.getText());
        exp.setFileNameContains(jTxt_FileNameContains.getText());
        exp.setSignalColNameContains(jTxt_sig_col_name_contains.getText());
        exp.setTimeBetSamples(Float.parseFloat(String.valueOf(jTx_time_bet_two_sample.getText())));
        exp.setTimeColumnNumber(Integer.parseInt(String.valueOf(jTxt_time_column_no.getText())));

        try {
            Utils.ReadFileStatus rfs = exp.ReadSignals();
            if(rfs == Utils.ReadFileStatus.FileIsReadCorrectly)
            {               
                if (this.sectionNames != null && this.sectionNames.size() > 0) {
                    exp.ExperimentHasTreatment = true;
                    exp.AddSectionToSignals(this.sectionNames.size(), this.sectionNames);
                    MakeVisibleTreatmentPanels();
                    exp.NumberOfSections = this.sectionNames.size();
                    
                } else {
                    exp.ExperimentHasTreatment = false;
                    MakeInvisibleTretmentPanels();
                    exp.AddOneSectionToEachSignal();
                    exp.NumberOfSections = 1;
                }
                activeComps_ByOpenSignal();
                signal_Index = 0;
                jLbl_Id.setText(String.valueOf(signal_Index + 1));
                jLblTotal.setText(String.valueOf(exp.signals.size()));
                jPnlPlot.setLayout(new BorderLayout());                
                this.createSignalPlot(exp.signals.get(0));               
                jBtnOpenSig.setEnabled(false);
                jTxtFrom.requestFocus();
                this.ExperimentIsOpen = true;  
                this.BrowseDefaultPath = new File(jTxtFolderName.getText().trim());
            } else {
                
                if(rfs == Utils.ReadFileStatus.EmptySiganlArray)
                {                
                    JOptionPane.showMessageDialog(this, "No signal is found!");
                }
                else if(rfs == Utils.ReadFileStatus.InputFileDoesNotHaveHeader)
                {
                    JOptionPane.showMessageDialog(this, "The number of header columns is not equal to the number of data columns."
                            + " Please correct input file.");
                }
                else if(rfs == Utils.ReadFileStatus.NotEqualNumbersForSignalandTime)
                {
                    JOptionPane.showMessageDialog(this, "The number of values in signal column is not equal to the number of values in time column."
                            + " Please correct input file.");
                }
                
                jBtnFolderBrowse.requestFocus();
                deactiveComps_ByframeLoad();
                this.ExperimentIsOpen = false;
            }
        } catch (InvalidFormatException ex) {
            Logger.getLogger(FrmSigAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrmSigAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jBtnOpenSigActionPerformed

    private void createSignalPlot(Signal sig) {
        if (this.exp != null && sig != null) {
            //this.ISI_TimePlot = new PlotPoints();
            this.signalPlot = sig.createSignalPlot(this.exp);
            if(this.signalPlot!=null)
                this.ShowSignalPlot();
            else
               JOptionPane.showMessageDialog(this, "number of values in time column is not equal to number of values in sinal column!"); 
        }
    }

    private void jBtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBackActionPerformed

        PressBackwardButton();

    }//GEN-LAST:event_jBtnBackActionPerformed

    private void PressBackwardButton() {
        if (Backward()) {
            jLbl_Id.setText(String.valueOf(Integer.parseInt(jLbl_Id.getText()) - 1));
            //SetjPnlTreatmentPeak();
        }
    }

    private void jBtnForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnForwardActionPerformed
        PressForwardButton();
    }//GEN-LAST:event_jBtnForwardActionPerformed

    private void PressForwardButton() {
        if (Forward()) {
            jLbl_Id.setText(String.valueOf(Integer.parseInt(jLbl_Id.getText()) + 1));
            //SetjPnlTreatmentPeak();
        }
    }

    public void setEditSignalBoxes(double From, double To, boolean FillTextBoxes) {
        if(FillTextBoxes)
        {                
           jTxtFrom.setText(Utils.DoubleToStringEmitFloats(From));
           jTxtTo.setText(Utils.DoubleToStringEmitFloats(To));
        }
        else
        {
           jTxtFrom.setText("");
           jTxtTo.setText("");
        }
    }

    private void EditSignalTime() {
        if (exp != null && exp.signals.size() > 0) {

            if (signal_Index > -1) {
                double from_time = -1;
                double to_time = -1;

                Signal signal = exp.signals.get(signal_Index);

                if (!"".equals(jTxtFrom.getText())) {
                    try {

                        from_time = Double.parseDouble(String.valueOf(jTxtFrom.getText()));

                    } catch (NumberFormatException exc) {
                        JOptionPane.showMessageDialog(this, "Please enter a number in the 'From' field");

                        jTxtFrom.setText("");
                        return;
                    }
                }
                if (!"".equals(jTxtTo.getText())) {
                    try {

                        to_time = Double.parseDouble(String.valueOf(jTxtTo.getText()));

                    } catch (NumberFormatException exc) {
                        JOptionPane.showMessageDialog(this, "Please enter a number in the 'To' field");

                        jTxtTo.setText("");
                        return;
                    }
                }
                if (to_time != -1 && from_time > to_time) {
                    JOptionPane.showMessageDialog(this, "'From' field should be smaller than 'To' field");
                    return;
                }

                if (!jCB_applyToAll_time.isSelected()) 
                {

                   // signal.EditSignalWithOriginalSignal(from_time, to_time, jCbxNormalize.isSelected());
                    signal.EditSignal(from_time, to_time);

                } 
                else
                {
                   if(!exp.EditExp(from_time, to_time)) {//edit time of all signals

                            JOptionPane.showMessageDialog(this, "time change did not applied on all signals!");
                    } 
                }
                this.UpdatePlots();
                this.jTxtFrom.setText("");
                this.jTxtTo.setText("");
                
                jCB_applyToAll_time.setSelected(false);
            } else {
                JOptionPane.showMessageDialog(this, "No signal exists ");
            }
            this.deactiveSelectPeakComps();
            EmptyISI_TimePanel();

        }
    }
    private void jBtnEditSignalTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditSignalTimeActionPerformed
        EditSignalTime();
    }//GEN-LAST:event_jBtnEditSignalTimeActionPerformed

    private void FindPeaks() {
        Signal sig = exp.signals.get(signal_Index);
        double peakThr = 0;
        try {
            if (!"".equals(jTxtPeakThr.getText())) {
                peakThr = Double.parseDouble(String.valueOf(jTxtPeakThr.getText()));
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(this, "Please enter a number in the 'peak threshold' field");
            jTxtPeakThr.setText("");
            return;
        }
       
        if (!jCB_applyToAllPeak.isSelected()) {
           sig.findPeaks(peakThr);
           
        } else {
            this.exp.findPeaks(peakThr);
           sig.getPeaknumber();
        }
        this.UpdatePlots();
        this.UpdateSigmaTavPlot();
        this.ShowPeaksInformations();   
        System.gc();
    }

    private void jBtnFindPeaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFindPeaksActionPerformed
        // TODO add your handling code here:       
        FindPeaks();
        
    }//GEN-LAST:event_jBtnFindPeaksActionPerformed

    private void SignalEdit_Reset() {
        Signal signal = exp.signals.get(signal_Index);
        if (!jCB_applyToAll_time.isSelected())//edit time of all signals 
        {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Do you like to reset the current signal?", "Reset Signal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                signal.ResetSignal();
            } else {
                return;
            }

        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Do you like to reset ALL signals in this experiment?", "Reset Signals", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.exp.ResetExpSignals();
                if (exp.DeletedSignalsList.size() > 0) {
                    for (int i = 0; i < exp.DeletedSignalsList.size(); i++) {
                        exp.signals.get(exp.DeletedSignalsList.get(i)).SetDeleteFlag(false);
                    }
                    exp.DeletedSignalsList.clear();
                }
                jLblTotal.setText(String.valueOf(exp.signals.size()));
                
            } else {
                return;
            }
        }       
        this.UpdatePlots();
        jTxtFrom.setText("");
        jTxtTo.setText("");
        jCB_applyToAll_time.setSelected(false);
        this.deactiveSelectPeakComps();
        this.SetjPnlTreatmentPeak();
        EmptyISI_TimePanel();
        jPnl_ISITime.setVisible(false);
    }
    private void JbtnSignalEdit_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnSignalEdit_ResetActionPerformed
        //By resetting th signal, All signal will change to the original signal
        //and All section objects will be deleted.
        // So the time of each section should be defined again.
        SignalEdit_Reset();
        
    }//GEN-LAST:event_JbtnSignalEdit_ResetActionPerformed

    private void jBtnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLastActionPerformed

        signal_Index = exp.signals.size();
        if (Backward()) {
            jLbl_Id.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
            SetjPnlTreatmentPeak();
        }
    }//GEN-LAST:event_jBtnLastActionPerformed

    private void jBtnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFirstActionPerformed

        signal_Index = -1;
        if (Forward()) {
            jLbl_Id.setText("1");
            SetjPnlTreatmentPeak();
        }
    }//GEN-LAST:event_jBtnFirstActionPerformed

    public void AddSelectedNadir() {
        Signal sig = exp.signals.get(signal_Index);
        if (sig.addNadir(exp)) {
            sig.createPeakPlot(this.exp, this.signalPlot); 
            this.ISI_TimePlot = sig.CreateISITimePlot();
            this.ShowSignalPlot();
            this.ShowISI_TimePlot(sig);
            UpdateSigmaTavPlot();
            UpdateControls();
        } else {
            JOptionPane.showMessageDialog(this, "Selected point cannot be added as a nadir.");
        }
    }
    
    public void AddSelectedPeak() {       
        Signal sig = exp.signals.get(signal_Index);
        if (sig.addPeak(exp)) {                
            sig.createPeakPlot(this.exp, this.signalPlot);
            this.ISI_TimePlot = sig.CreateISITimePlot();
            this.ShowSignalPlot();
            this.ShowISI_TimePlot(sig);
            UpdateSigmaTavPlot();
            UpdateControls();
        } else {
            JOptionPane.showMessageDialog(this, "Selected point cannot be added as a peak.");
        }       
    }

    public void RemoveSelectedPeakorNadir() {        
        Signal sig = exp.signals.get(signal_Index);
        if (sig.removePeakorNadir(exp)) {
            sig.createPeakPlot(this.exp, this.signalPlot); // this function find peaks and add peaks to the current signalPlot object
            this.ISI_TimePlot = sig.CreateISITimePlot();
            this.ShowSignalPlot();
            this.ShowISI_TimePlot(sig);
            UpdateSigmaTavPlot();
            UpdateControls();
        } else {
            JOptionPane.showMessageDialog(this, "selected point is not a peak.");
        }       
    }

    private void jTxtTreatmentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtTreatmentFocusLost
        // TODO add your handling code here:
        if (this.exp == null) {
            final int numberOfTreatments;
            try {
                if (!"".equals(jTxtTreatment.getText()) || !"0".equals(jTxtTreatment.getText())) {
                    numberOfTreatments = Integer.parseInt(String.valueOf(jTxtTreatment.getText()));
                    if (numberOfTreatments < 4) {
                        if (numberOfTreatments != 0) {
                            if (FDefineTreat == null) {
                                if (this.sectionNames == null) {
                                    this.sectionNames = new ArrayList<>();
                                }
                                FDefineTreat = new FDefineTreatmentName(numberOfTreatments, this.sectionNames);
                                FDefineTreat.setVisible(true);
                                FDefineTreat.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                                FDefineTreat.addWindowListener(new java.awt.event.WindowAdapter() {
                                    @Override
                                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                                        FDefineTreat.dispose();
                                        FDefineTreat = null;
                                        jBtnOpenSig.requestFocus();

                                    }
                                });
                            }
                        } else {
                            this.sectionNames = null;
                            if (this.FDefineTreat != null) {
                                this.FDefineTreat.dispose();
                                this.FDefineTreat = null;
                            }
                            //jBtnOpenSig.requestFocus();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Number of treatments cannot be more than 3!");
                        jTxtTreatment.requestFocus();
                    }
                } else {
                    jTxtTreatment.setText("0");
                    this.sectionNames = null;
                    if (this.FDefineTreat != null) {
                        this.FDefineTreat.dispose();
                        this.FDefineTreat = null;
                    }
                    //jBtnOpenSig.requestFocus();
                }

            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(this, "Please enter an number in the 'number of treatment' field");
                jTxtTreatment.setText("0");
            }
        }
    }//GEN-LAST:event_jTxtTreatmentFocusLost

    private void AssignSection() {
        if (exp != null && exp.signals.size() > 0) {
            if (signal_Index > -1) {

                Signal signal = exp.signals.get(signal_Index);
                double Min_Time = Utils.findMin(signal.sig_time);
                double Max_Time = Utils.findMax(signal.sig_time);
                double timeFrom;
                double timeTo;
                if (jTxtSectionFrom.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please specify the begining point of section");
                    return;
                }
                if (jTxtSextionTo.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please specify end point of section");
                    return;
                }
                try {

                    timeFrom = Double.parseDouble(String.valueOf(jTxtSectionFrom.getText()));

                } catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(this, "Please enter a number in the 'From' field");
                    jTxtSectionFrom.setText("");
                    return;
                }
                try {

                    timeTo = Double.parseDouble(String.valueOf(jTxtSextionTo.getText()));

                } catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(this, "Please enter a number in the 'To' field");
                    jTxtSextionTo.setText("");
                    return;
                }

                if (timeFrom > timeTo) {
                    JOptionPane.showMessageDialog(this, "'From' field should be smaller than 'To' field");
                    return;
                }

                if (timeFrom < Min_Time) {

                    timeFrom = Min_Time;
                    jTxtSectionFrom.setText((String.valueOf(Min_Time)));
                }
                if (timeTo > Max_Time) {

                    timeTo = Max_Time;
                    jTxtSextionTo.setText(String.valueOf(Max_Time));
                }

                int SectionID = Integer.parseInt(String.valueOf(this.jComBSectionNamesP1.getSelectedIndex()));
                boolean result = true;

                if (!jCbxSectionApplyToAll.isSelected()) {
                    result = signal.AddTimeAndValueToSections(timeFrom, timeTo, SectionID);

                    if (!result) {
                        JOptionPane.showMessageDialog(this, "The specified time range did not applied to the selected section of current signals."
                                + " The specified time range has overlap to another section at this signal!");
                    }
                } else {
                    result = this.exp.AddTimeAndValueToSections(timeFrom, timeTo, SectionID);

                    if (!result) {
                        JOptionPane.showMessageDialog(this, "The specified time range did not applied to the selected section of all signals."
                                + " The specified time range has overlap to another section in one signal!");
                    }
                }
                if (result) {
                    jTxtSectionFrom.setText("");
                    jTxtSextionTo.setText("");
                    signal.AddSectionsToSignalPlot(SectionID, signalPlot);
                    ShowSignalPlot();
                    if (signal.IsTimeAddedToAllSections()) {
                        EnablejPnlTreatmentPeak();
                    }
                }
            }
        }
    }
    private void jBtnAssignSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAssignSectionActionPerformed
        AssignSection();
    }//GEN-LAST:event_jBtnAssignSectionActionPerformed

    private void SectionFindPeaks() {
        Signal sig = exp.signals.get(signal_Index);
        double peakThr = 0;
        try {
            if (!"".equals(jTxtSectionPeakThreshold.getText())) {
                peakThr = Double.parseDouble(String.valueOf(jTxtSectionPeakThreshold.getText()));
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(this, "Please enter a number in the 'peak threshold' field");

            jTxtSectionPeakThreshold.setText("");
            return;
        }
        int SectionID = Integer.parseInt(String.valueOf(this.jComBSectionNamesP2.getSelectedIndex()));
        if (!jCbxApplyToAllPeakSection.isSelected()) {
            sig.findPeaks(peakThr, SectionID);
           
        } else {
            this.exp.findPeaks(peakThr, SectionID);
        }
        
        sig.createPeakPlot(this.exp, this.signalPlot);
        this.ISI_TimePlot = sig.CreateISITimePlot();
        this.ShowSignalPlot();
        this.ShowISI_TimePlot(sig);
        this.UpdateSigmaTavPlot();
        this.ShowPeaksInformations();
    }
    private void jBtnSectionFindPeaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSectionFindPeaksActionPerformed
        SectionFindPeaks();
        
    }//GEN-LAST:event_jBtnSectionFindPeaksActionPerformed

    private void jBtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDeleteActionPerformed

       DeleteSignal();
//        Signal sig = exp.signals.get(signal_Index);
//        sig.locateSectionMaxPeaksInSignalArray();
    }//GEN-LAST:event_jBtnDeleteActionPerformed

    private void jBtnUndoDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnUndoDelActionPerformed
        UndoLastDelete();
    }//GEN-LAST:event_jBtnUndoDelActionPerformed

    private void jComBSectionNamesP3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComBSectionNamesP3ItemStateChanged
        // TODO add your handling code here:
        if (jComBSectionNamesP3.getItemCount() > 0 && this.signal_Index > -1) {
            ShowAMP_SW(this.exp.signals.get(this.signal_Index));
        }

    }//GEN-LAST:event_jComBSectionNamesP3ItemStateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        int Height = this.jPnlPlotHolders.getHeight();
        int width = this.jPnlPlotHolders.getWidth();

        int HPlot = (int) Math.floor(0.5 * Height);
        int HISI_Time = (int) Math.floor(0.37 * Height);
        int H_Control = Height - (HPlot + HISI_Time);
        this.jPnlControl.setPreferredSize(new Dimension(width, H_Control));
        this.jPnlPlot.setPreferredSize(new Dimension(width, HPlot));
        this.jPnl_ISITime.setPreferredSize(new Dimension(width, HISI_Time));


    }//GEN-LAST:event_formComponentResized

    private void jBtnForwardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnForwardKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            this.jBtnDelete.requestFocus();
            DeleteSignal();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            this.jBtnUndoDel.requestFocus();
            UndoLastDelete();

        }

    }//GEN-LAST:event_jBtnForwardKeyPressed

    private void jBtnBackKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnBackKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            this.jBtnDelete.requestFocus();
            DeleteSignal();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            this.jBtnUndoDel.requestFocus();
            UndoLastDelete();

        }
    }//GEN-LAST:event_jBtnBackKeyPressed

    private void jBtnDeleteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnDeleteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            this.jBtnUndoDel.requestFocus();
            UndoLastDelete();

        }
    }//GEN-LAST:event_jBtnDeleteKeyPressed

    private void jBtnUndoDelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnUndoDelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            this.jBtnDelete.requestFocus();
            DeleteSignal();
        }
    }//GEN-LAST:event_jBtnUndoDelKeyPressed

    private void jCbxSectionApplyToAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCbxSectionApplyToAllKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jCbxSectionApplyToAll.setSelected(true);
        }
    }//GEN-LAST:event_jCbxSectionApplyToAllKeyPressed

    private void jCB_applyToAll_timeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCB_applyToAll_timeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jCB_applyToAll_time.setSelected(true);
        }
    }//GEN-LAST:event_jCB_applyToAll_timeKeyPressed

    private void jCB_applyToAllPeakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCB_applyToAllPeakKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jCB_applyToAllPeak.setSelected(true);
        }
    }//GEN-LAST:event_jCB_applyToAllPeakKeyPressed

    private void jCbxApplyToAllPeakSectionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCbxApplyToAllPeakSectionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jCbxApplyToAllPeakSection.setSelected(true);
        }
    }//GEN-LAST:event_jCbxApplyToAllPeakSectionKeyPressed

    private void jBtnFirstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnFirstKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            this.jBtnDelete.requestFocus();
            DeleteSignal();
        }
        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            this.jBtnUndoDel.requestFocus();
            UndoLastDelete();

        }
    }//GEN-LAST:event_jBtnFirstKeyPressed

    private void jBtnLastKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnLastKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            this.jBtnDelete.requestFocus();
            DeleteSignal();
        }
        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            this.jBtnUndoDel.requestFocus();
            UndoLastDelete();

        }
    }//GEN-LAST:event_jBtnLastKeyPressed

    private void jBtnEditSignalTimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnEditSignalTimeKeyPressed
        // TODO add your handling code here:
        applyLeftRightBtn(evt);
    }//GEN-LAST:event_jBtnEditSignalTimeKeyPressed

    private void JbtnSignalEdit_ResetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JbtnSignalEdit_ResetKeyPressed
        // TODO add your handling code here:
        applyLeftRightBtn(evt);
    }//GEN-LAST:event_JbtnSignalEdit_ResetKeyPressed

    private void jBtnAssignSectionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnAssignSectionKeyPressed
        // TODO add your handling code here:
        applyLeftRightBtn(evt);
    }//GEN-LAST:event_jBtnAssignSectionKeyPressed

    private void jBtnSectionFindPeaksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnSectionFindPeaksKeyPressed
        // TODO add your handling code here:
        applyLeftRightBtn(evt);
    }//GEN-LAST:event_jBtnSectionFindPeaksKeyPressed

    private void jBtnFindPeaksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtnFindPeaksKeyPressed
        // TODO add your handling code here:
        applyLeftRightBtn(evt);
    }//GEN-LAST:event_jBtnFindPeaksKeyPressed

    private void jTx_time_bet_two_sampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTx_time_bet_two_sampleActionPerformed
        // TODO add your handling code here:
        if (this.ExperimentIsOpen && this.exp!=null) {
            try {
                if (!"".equals(jTx_time_bet_two_sample.getText())) {
                    this.exp.EditTimeBetSamples(Float.parseFloat(String.valueOf(jTx_time_bet_two_sample.getText())));
                    UpdatePlots();
                    ShowAMP_SW(this.exp.signals.get(signal_Index));
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(this, "Please enter a number in the 'Time Between Two Image' field");

                jTx_time_bet_two_sample.setText("1");
                jTx_time_bet_two_sample.requestFocus();
            }
        }
    }//GEN-LAST:event_jTx_time_bet_two_sampleActionPerformed
    
    private void applyLeftRightBtn(java.awt.event.KeyEvent evt)
    {
       if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.jBtnForward.requestFocus();
            PressForwardButton();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.jBtnBack.requestFocus();
            PressBackwardButton();
           
        } 
    }
    public void ShowSelectedSignalOnSigmaTav(int signalIdx) {
        this.signal_Index = signalIdx;
        int SignalId = this.signal_Index + 1;
        UpdatePlots();
        int count = 0;
        for (Integer DeletedSignalsList : exp.DeletedSignalsList) {
            if (SignalId > DeletedSignalsList) {
                count++;
            }
        }

        for (Integer FilteredSignalsList : exp.FilteredSignalsList) {
            if (SignalId > FilteredSignalsList) {
                count++;
            }
        }

        jLbl_Id.setText(String.valueOf(SignalId - count));
    }

    private boolean Forward() {
        int StoreSignalIndex = signal_Index;
        if (signal_Index < (exp.signals.size() - 1)) {

            do {
                signal_Index = signal_Index + 1;

            } while (signal_Index < (exp.signals.size()) && (exp.signals.get(signal_Index).IsDeleted() || exp.signals.get(signal_Index).IsFiltered()));

            if (signal_Index < (exp.signals.size())) {
                UpdatePlots();
                UpdateControls();
                return true;
            } else {
                signal_Index = StoreSignalIndex;
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean Backward() {
        if (signal_Index > 0) {

            int storeSignalIndex = signal_Index;
            do {
                signal_Index = signal_Index - 1;

            } while (signal_Index > -1 && (exp.signals.get(signal_Index).IsDeleted() || exp.signals.get(signal_Index).IsFiltered()));

            if (signal_Index > -1) {
                UpdatePlots();
                UpdateControls();
                return true;
            } else {
                signal_Index = storeSignalIndex;
                return false;
            }
        } else {
            return false;
        }

    }
    
    private void UpdateControls()
    {
        jTxtFrom.setText("");
        jTxtTo.setText("");
        jCB_applyToAll_time.setSelected(false);
        
    }
    private void UpdatePlots() { // remove the current plot and draw the new plot

        this.signalPlot=null;
        this.ISI_TimePlot = null;
        this.signalPlot = exp.signals.get(signal_Index).createSignalPlot(exp);
        this.ISI_TimePlot = exp.signals.get(signal_Index).CreateISITimePlot();
        this.ShowSignalPlot();          
        this.ShowISI_TimePlot(exp.signals.get(signal_Index));
     
    }

    private void ShowSignalPlot() {
        if(signalPlot!= null)
        {
            jPnlPlot.removeAll();
            
            jPnlPlot.add(this.signalPlot.Signalcpanel);
            
            jPnlPlot.validate();
            jPnlPlot.repaint();
        }
        else
        {
             JOptionPane.showMessageDialog(this, "Signal cannot be shown!");
        }
    }

    private void ShowISI_TimePlot(Signal sig) {

        if (this.ISI_TimePlot != null) {
            jPnl_ISITime.removeAll();
            jPnl_ISITime.repaint();
            jPnl_ISITime.add(this.ISI_TimePlot.chartPanel);
            jPnl_ISITime.validate();            
        } 
        else
        {
           EmptyISI_TimePanel(); 
        }
        this.ShowPeaksInformations();
        
    }

    private void ShowAMP_SW(Signal sig) {
        if (sig.getPeaknumber()>0) {
           
            int SectionIndex = -1;
            if (sig.getNemberofSections() > 1) {
                SectionIndex = jComBSectionNamesP3.getSelectedIndex();
            } else {
                SectionIndex = 0;
            }
            double MeanSW = sig.sections.get(SectionIndex).getMeanSW();
            double SW_SD = sig.sections.get(SectionIndex).getSpikeWidthSD();

            double MeanAMP = sig.sections.get(SectionIndex).getMeanAMP();
            double AMP_SD = sig.sections.get(SectionIndex).getAmplitudeSD();
          
            if (sig.getNemberofSections() > 1) {
                this.jLblSpikeWidthSec.setText(" " + Utils.DoubleToString(MeanSW));
                this.jLblSpikeWidthSDSec.setText(" " + Utils.DoubleToString(SW_SD));
                this.jLblAmplitudeSec.setText(" " + Utils.DoubleToString(MeanAMP));
                this.jLblAmplitudeSDSec.setText(" " + Utils.DoubleToString(AMP_SD));
            } else {
                this.jLblSpikeWidth.setText(" " + Utils.DoubleToString(MeanSW));
                this.jLblSpikeWidthSD.setText(" " + Utils.DoubleToString(SW_SD));
                this.jLblAmplitude.setText(" " + Utils.DoubleToString(MeanAMP));
                this.jLblAmplitudeSD.setText(" " + Utils.DoubleToString(AMP_SD));
            }
          
        } else {
            EmptyLabelsAMP_SW();
        }
    }

    private void EmptyLabelsAMP_SW() {
        this.jLblSpikeWidth.setText("");
        this.jLblAmplitude.setText("");
        this.jLblSpikeWidthSD.setText("");
        this.jLblAmplitudeSD.setText("");
        this.jLblSpikeWidthSec.setText("");
        this.jLblAmplitudeSec.setText("");
        this.jLblSpikeWidthSDSec.setText("");
        this.jLblAmplitudeSDSec.setText("");
    }

    private void EmptyISI_TimePanel() {
        jPnl_ISITime.removeAll();
        jPnl_ISITime.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jPnl_ISITime.repaint();
        jPnl_ISITime.validate();
        jPnl_ISITime.setVisible(false);
        
    }

    private void EmptySignalPanel() {
        jPnlPlot.removeAll();
        jPnlPlot.repaint();
        jPnlPlot.validate();
        //jPnlPlot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void ExportDataFigs() throws Exception {
        FrmSaveDialog frmsave = new FrmSaveDialog(this,this.exp,this.SaveDefaultPath);
        frmsave.setVisible(true);
    }

    private void DeleteSignal() {
        if (signal_Index < exp.signals.size() && signal_Index > -1) {
            exp.signals.get(signal_Index).SetDeleteFlag(true);
            exp.DeletedSignalsList.add(signal_Index);
            if (Forward()) {
                jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
            } else {
                if (Backward()) {
                    jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
                    jLbl_Id.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
                } else {
                    EmptyISI_TimePanel();
                    EmptySignalPanel();
                    jLblTotal.setText("0");
                    jLbl_Id.setText("0");
                    deactiveSelectPeakComps();
                    DisablejPnlTreatmentPeak();
                    DisablejPnlTreatment();
                    DisablejPnlEditSignal();
                    DisablePnlSignalPeak();

                }

            }
            UpdateSigmaTavPlot();
        }
    }

    private void UndoLastDelete() {
        if (exp.DeletedSignalsList.size() > 0) {
            int Last_elementIdx = exp.DeletedSignalsList.size() - 1;
            signal_Index = exp.DeletedSignalsList.get(Last_elementIdx);
            exp.DeletedSignalsList.remove(Last_elementIdx);

            exp.signals.get(signal_Index).SetDeleteFlag(false);
            UpdatePlots();

            jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
            int SignalId = signal_Index + 1;
            int count = 0;
            for (Integer DeletedSignalsList : exp.DeletedSignalsList) {
                if (SignalId > DeletedSignalsList) {
                    count++;
                }
            }

            for (Integer FilteredSignalsList : exp.FilteredSignalsList) {
                if (SignalId > FilteredSignalsList) {
                    count++;
                }
            }

            jLbl_Id.setText(String.valueOf(SignalId - count));
            UpdateSigmaTavPlot();
            jBtnFindPeaks.setEnabled(true);
            EnablejPnlTreatment();
            SetjPnlTreatmentPeak();
            EnablejPnlEditSignal();
            EnablePnlSignalPeak();
        }
    }

    /**
     *
     */
    public void ShowUnFilteredSignals() {
        boolean sigIndexFound = false;
        for (Signal sig : this.exp.signals) {
            if (!sig.IsFiltered() && !sig.IsDeleted()) {
                signal_Index = sig.getSig_Id() - 1;
                sigIndexFound = true;
                break;
            }
        }
        if (sigIndexFound) {
            UpdatePlots();
            jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
            jLbl_Id.setText("1");
            UpdateSigmaTavPlot();
        } else {
            EmptyISI_TimePanel();
            EmptySignalPanel();
            jLblTotal.setText("0");
            jLbl_Id.setText("0");
            deactiveSelectPeakComps();
            DisablejPnlTreatmentPeak();
            DisablejPnlTreatment();
            DisablejPnlEditSignal();
            DisablePnlSignalPeak();
        }
    }

    public void ShowSignalsRemoveFilter() {
        signal_Index = -1;
        this.Forward();
        UpdateSigmaTavPlot();
        jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
        jLbl_Id.setText(String.valueOf(signal_Index + 1));

    }

    private void UndoAllDeletes() {
        if (exp.DeletedSignalsList.size() > 0) {
            for (Integer DeletedSignalsList : exp.DeletedSignalsList) {
                exp.signals.get(DeletedSignalsList).SetDeleteFlag(false);
            }
            exp.DeletedSignalsList.clear();
            signal_Index = 0;
            UpdatePlots();

            jLblTotal.setText(String.valueOf(exp.countUnDeletedUnFilteredSignals()));
            jLbl_Id.setText(String.valueOf(signal_Index + 1));

            UpdateSigmaTavPlot();
            jBtnFindPeaks.setEnabled(true);
            jBtnEditSignalTime.setEnabled(true);
            JbtnSignalEdit_Reset.setEnabled(true);
            EnablejPnlTreatment();
            SetjPnlTreatmentPeak();
            EnablejPnlEditSignal();
            EnablePnlSignalPeak();
        }
    }

    private void PlotSigmaTav() {
        if (this.exp.plotSigmaTavPlot()) {
            this.FSigmaTav = null;
            FSigmaTav = new FSigma_Tav(this.exp);
            FSigmaTav.setVisible(true);
            FSigmaTav.ShowSigmaTavPlot();// show plot on the panel
            FSigmaTav.setPlotInfoTextBoexs(0);
        } else {
            JOptionPane.showMessageDialog(this, "There is no signal with identified peak!");
        }
    }

    private void UpdateSigmaTavPlot() {
        if (this.exp.SigmaTavShown) {
            if (this.exp.plotSigmaTavPlot()) {
                FSigmaTav.ShowSigmaTavPlot(); // show plot on the panel
            } else {
                this.FSigmaTav.dispose();
                this.exp.SigmaTavShown = false;
            }
        }
    }

    private void DisposeForm() {
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to close this experiment?", "Exitting Window", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (this.FDefineTreat != null) {
                this.FDefineTreat.dispose();
                this.FDefineTreat = null;
            }
            if (this.FSigmaTav != null) {
                this.FSigmaTav.dispose();
                this.FSigmaTav = null;
            }
            this.dispose();
            System.gc();
        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }
    
    private void CloseExperiment()
    {
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to close this experiment?", "Close Experiment", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (this.FDefineTreat != null) {
                this.FDefineTreat.dispose();
                this.FDefineTreat = null;
            }
            if (this.FSigmaTav != null) {
                this.FSigmaTav.dispose();
                this.FSigmaTav = null;
            }
           EmptyExperimentComponents();
           this.deactiveComps_ByframeLoad(); 
           System.gc();          
        }
    }
    
    private void SaveExperiment()
    {        
        JDialog f1 = new JDialog(this,"Save Experiment",true);
        f1.setSize(new Dimension(460,130));
        f1.setResizable(false);
        f1.setAlwaysOnTop(true);
        f1.setLocationRelativeTo(null);        
        Pnl_SaveExperiment Pse = new Pnl_SaveExperiment(this.exp,f1);
        f1.setContentPane(Pse);        
        f1.setVisible(true);      
    }
       
    private void ExitProgram() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the tools?", "Exitting Application", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void RemoveBG()
    {        
        if(signal_Index !=-1 && exp!=null)
        {            
            Signal sig = this.exp.signals.get(signal_Index);
            if(sig.IsPeakComputedForAllSections())
            {
                if(!sig.IsNormalized)
                {                    
                    JDialog dialog = new JDialog(this, "", Dialog.ModalityType.DOCUMENT_MODAL);
                    FrmRemoveBackgorund frmRemoveBG = new FrmRemoveBackgorund(this.exp,signal_Index,dialog);
                    dialog.setContentPane(frmRemoveBG.getContentPane());
                    
                    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    
                    this.UpdatePlots();
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"Signal is normalized once!"
                            + " For removing background or doing normalization again, first you should reset your signal"
                            + " to its initial values");
                }
            }
            else
            {
                 JOptionPane.showMessageDialog(this,"Please first find peaks of your signal.");
            }
        }
    }

    private void createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        nMenuItem.setMnemonic(KeyEvent.VK_N);
        nMenuItem.setToolTipText("New Experiment");
        nMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //NewExperiment().setVisible(true);
                System.gc();
                FrmSigAnalysis frame = new FrmSigAnalysis();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });

        CMenuItem.setMnemonic(KeyEvent.VK_C);
        CMenuItem.setToolTipText("Close This Experiment");
        
        CMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                CloseExperiment();
                
            }
        });

        SMenuItem.setMnemonic(KeyEvent.VK_S);
        SMenuItem.setToolTipText("Save Experiment");
        SMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // save experiment data (metadata) somewhere in order to be able to open again
                try {
                    
                    SaveExperiment();
                                     
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        exMenuItem.setMnemonic(KeyEvent.VK_X);
        exMenuItem.setToolTipText("Export data and figures");
        exMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    ExportDataFigs(); //export data which is useful for user
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ExitProgram();
            }
        });

        file.add(nMenuItem);
        file.add(CMenuItem);
        file.addSeparator();
        file.add(SMenuItem);
        file.add(exMenuItem);
        file.addSeparator();
        file.add(eMenuItem);

        menubar.add(file);

        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        
       
        RemoveBgMenuItem.setToolTipText("Remove background");
        RemoveBgMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RemoveBG();
            }
        });

        DMenuItem.setMnemonic(KeyEvent.VK_D);
        DMenuItem.setToolTipText("Delete the current signal");
        DMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                DeleteSignal();

            }
        });

        UnDMenuItem.setMnemonic(KeyEvent.VK_Z);
        UnDMenuItem.setToolTipText("Undo the last delete");
        UnDMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                UndoLastDelete();
            }
        });

        UnDAllMenuItem.setMnemonic(KeyEvent.VK_U);
        UnDAllMenuItem.setToolTipText("Exit application");
        UnDAllMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                UndoAllDeletes();
            }
        });
        
        edit.add(RemoveBgMenuItem);
        edit.addSeparator();
        edit.add(DMenuItem);
        edit.add(UnDMenuItem);
        edit.add(UnDAllMenuItem);
        menubar.add(edit);

        JMenu plot = new JMenu("Plot");
        plot.setMnemonic(KeyEvent.VK_P);

        PTavSigForExpMenuItem.setToolTipText("Plot standard deviations of ISIs vs. average of ISIs for whole signals");
        PTavSigForExpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                PlotSigmaTav();
            }
        });
        plot.add(PTavSigForExpMenuItem);
        menubar.add(plot);
        
        JMenu CASA = new JMenu("CaSiAn");
        AboutMenuItem.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent event) {
                FrmAboutCASA frmCasa = new FrmAboutCASA();
                frmCasa.setVisible(true);
                   
        }
        });
        CASA.add(AboutMenuItem);       
        menubar.add(CASA);
        
        setJMenuBar(menubar);
        //System.setProperty("apple.laf.useScreenMenuBar", "true");
//        MacMenuHandler macController = new MacMenuHandler();
//        String osName = System.getProperty("os.name").toLowerCase();
//        boolean isMacOs = osName.startsWith("mac os x");
        
        //boolean isMacOS = System.getProperty("mrj.version") != null;
//        if (isMacOs)
//        {
//          MRJApplicationUtils.registerAboutHandler(macController);
//          MRJApplicationUtils.registerPrefsHandler(macController);
//          MRJApplicationUtils.registerQuitHandler(macController);
//        }
    }

    /**
     * @param args the command line arguments
     */

    private void SetLayout() {
        
        this.setLayout(new BorderLayout());
        jPnlTop.setPreferredSize(new Dimension(1070, 116));
        this.add(jPnlTop, BorderLayout.NORTH);
        
        JScrollPane scorollbar = new JScrollPane(jPnlFunction, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scorollbar.setPreferredSize(new Dimension(322, 572));
        this.add(scorollbar, BorderLayout.EAST);
        jPnlEditSignal.setPreferredSize(new Dimension(305, 80));
        jPnlTreatment.setPreferredSize(new Dimension(305, 110));
        PnlSignalPeak.setPreferredSize(new Dimension(305, 80));
        jPnlTreatmentPeak.setPreferredSize(new Dimension(305, 110));
        jPnlSignalInfoWithoutSection.setPreferredSize(new Dimension(305, 70));
        
        jPnlSignalInfoWithSection.setPreferredSize(new Dimension(305, 100));

        jPnlPlotHolders.setPreferredSize(new Dimension(750, 610));
        this.add(jPnlPlotHolders, BorderLayout.CENTER);

        jPnlPlotHolders.setLayout(new BorderLayout());
        jPnlPlot.setPreferredSize(new Dimension(740, 290));
        jPnlPlotHolders.add(jPnlPlot, BorderLayout.CENTER);
        jPnl_ISITime.setPreferredSize(new Dimension(740, 220));
        jPnlPlotHolders.add(jPnl_ISITime, BorderLayout.NORTH);
        jPnlControl.setPreferredSize(new Dimension(740, 86));
        jPnlPlotHolders.add(jPnlControl, BorderLayout.SOUTH);

        jPnlControl.setLayout(new GridBagLayout());
        jPnlControl.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPnlButtons.setPreferredSize(new Dimension(730, 70));
        jPnlControl.add(jPnlButtons);

        this.pack();
    }

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JbtnSignalEdit_Reset;
    private javax.swing.JPanel PnlSignalPeak;
    private javax.swing.JButton jBtnAssignSection;
    private javax.swing.JButton jBtnBack;
    private javax.swing.JButton jBtnDelete;
    private javax.swing.JButton jBtnEditSignalTime;
    private javax.swing.JButton jBtnFindPeaks;
    private javax.swing.JButton jBtnFirst;
    private javax.swing.JButton jBtnFolderBrowse;
    private javax.swing.JButton jBtnForward;
    private javax.swing.JButton jBtnLast;
    private javax.swing.JButton jBtnOpenSig;
    private javax.swing.JButton jBtnSectionFindPeaks;
    private javax.swing.JButton jBtnUndoDel;
    private javax.swing.JCheckBox jCB_applyToAllPeak;
    private javax.swing.JCheckBox jCB_applyToAll_time;
    private javax.swing.JCheckBox jCbxApplyToAllPeakSection;
    private javax.swing.JCheckBox jCbxSectionApplyToAll;
    private javax.swing.JComboBox jComBSectionNamesP1;
    private javax.swing.JComboBox jComBSectionNamesP2;
    private javax.swing.JComboBox jComBSectionNamesP3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLblAMP;
    private javax.swing.JLabel jLblAMPSec;
    private javax.swing.JLabel jLblAMP_SD;
    private javax.swing.JLabel jLblAMP_SDSec;
    private javax.swing.JLabel jLblAmp;
    private javax.swing.JLabel jLblAmplitude;
    private javax.swing.JLabel jLblAmplitudeSD;
    private javax.swing.JLabel jLblAmplitudeSDSec;
    private javax.swing.JLabel jLblAmplitudeSec;
    private javax.swing.JLabel jLblAsFrom;
    private javax.swing.JLabel jLblAsTo;
    private javax.swing.JLabel jLblOf;
    private javax.swing.JLabel jLblPeakThr;
    private javax.swing.JLabel jLblPt;
    private javax.swing.JLabel jLblSW;
    private javax.swing.JLabel jLblSWSD;
    private javax.swing.JLabel jLblSWSDSec;
    private javax.swing.JLabel jLblSWSec;
    private javax.swing.JLabel jLblSection;
    private javax.swing.JLabel jLblSectionAssign;
    private javax.swing.JLabel jLblSectionNames3;
    private javax.swing.JLabel jLblSpikeWidth;
    private javax.swing.JLabel jLblSpikeWidthSD;
    private javax.swing.JLabel jLblSpikeWidthSDSec;
    private javax.swing.JLabel jLblSpikeWidthSec;
    private javax.swing.JLabel jLblTimeFrom;
    private javax.swing.JLabel jLblTimeTo;
    private javax.swing.JLabel jLblTotal;
    private javax.swing.JLabel jLbl_Id;
    private javax.swing.JLabel jLblmax;
    private javax.swing.JPanel jPnlButtons;
    private javax.swing.JPanel jPnlControl;
    private javax.swing.JPanel jPnlEditSignal;
    private javax.swing.JPanel jPnlFake;
    private javax.swing.JPanel jPnlFunction;
    private javax.swing.JPanel jPnlPlot;
    private javax.swing.JPanel jPnlPlotHolders;
    private javax.swing.JPanel jPnlSignalInfoWithSection;
    private javax.swing.JPanel jPnlSignalInfoWithoutSection;
    private javax.swing.JPanel jPnlTop;
    private javax.swing.JPanel jPnlTreatment;
    private javax.swing.JPanel jPnlTreatmentPeak;
    private javax.swing.JPanel jPnl_ISITime;
    private javax.swing.JPopupMenu jPopMenu_signalPlot;
    private javax.swing.JTextField jTx_time_bet_two_sample;
    private javax.swing.JTextField jTxtFolderName;
    private javax.swing.JTextField jTxtFrom;
    private javax.swing.JTextField jTxtPeakThr;
    private javax.swing.JTextField jTxtSectionFrom;
    private javax.swing.JTextField jTxtSectionPeakThreshold;
    private javax.swing.JTextField jTxtSextionTo;
    private javax.swing.JTextField jTxtTo;
    private javax.swing.JTextField jTxtTreatment;
    private javax.swing.JTextField jTxt_FileNameContains;
    private javax.swing.JTextField jTxt_sig_col_name_contains;
    private javax.swing.JTextField jTxt_time_column_no;
    // End of variables declaration//GEN-END:variables

    void jLblSelectedSignalValue(Double get) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class MyFocusTraversalPolicy extends FocusTraversalPolicy {

        @Override
        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int focusNumber = focusList.indexOf(aComponent);
            do {
                focusNumber = (focusNumber + 1) % focusList.size();

            } while (focusList.get(focusNumber).isEnabled() == false);
            return focusList.get(focusNumber);
        }

        @Override
        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int focusNumber = focusList.indexOf(aComponent);
            do {
                focusNumber = (focusList.size() + focusNumber - 1) % focusList.size();
            } while (focusList.get(focusNumber).isEnabled() == false);
            return focusList.get(focusNumber);
        }

        @Override
        public Component getDefaultComponent(Container focusCycleRoot) {
            return focusList.get(0);
        }

        @Override
        public Component getLastComponent(Container focusCycleRoot) {
            return focusList.get(focusList.size() - 1);
        }

        @Override
        public Component getFirstComponent(Container focusCycleRoot) {
            return focusList.get(0);
        }
    }

}
