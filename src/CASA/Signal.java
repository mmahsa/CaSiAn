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
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 *
 * @author mahsa.moein
 */
public class Signal{

    private int sig_Id;
    private int columnNo;
    private String name;
    public double ZoomedMinX;
    public double ZoomedMinY;
    public double ZoomedMaxX;
    public double ZoomedMaxY;
    public boolean PlotIsZoomed;

    public ArrayList<Sections> sections;
    
    public int NumberOfSections;
    
    public ArrayList<String> NameOfSections;

    private boolean IsDeleted;
    
    private boolean IsFiltered;

    public ArrayList<Double> sig_time; // the time array which we can edit it  
    public ArrayList<Double> sig_value;
    public ArrayList<Double> Origin_sig_value;
    public ArrayList<Double> Origin_sig_time;   
    private boolean SignalHasOneSection;
    //public boolean IsPeakShownForAllSections;   
    private int selectedSectionID;
    private int selectedIndex;
    private boolean IsIndexIn_Signal;
    private boolean IsIndexIn_Peaks;  
    private boolean IsIndexIn_Nadirs;
    public boolean IsPeakFound;    
    public boolean IsNormalized_temp;
    public boolean IsNormalized;
    
    public boolean IsBackgroundRemoved_temp;
    public boolean SigBGRemoved;
    private boolean IsFitParamComputed;
    public double[] FittedParamToBackground;
    public EnumType.CurveType FittedCurve;
    private double[] FittedCurve_Y;
    
    public boolean IsAverageBaseLineComputed;
    public double AverageBaseLine;
    
    private boolean IsSpikeBaseLinesComputed;
    private double[] Y_Spikebasline;
    //this array contains the index of all max peaks in the signal
    private ArrayList<Integer> sig_MaxPeak_Index;
    private double PeakPercent;
    
    public double[] getFittedCurve_Y()
    {
        this.ComputeFittedCurve_Y();
        return FittedCurve_Y;
    }
    
    public int getNumTimeDigits()
    {
      return String.valueOf(Utils.findMax(this.Origin_sig_time)).length();
    }
    
    public int getNumValueDigits()
    {
        return String.valueOf(Utils.findMax(this.Origin_sig_value)).length();
    }
    
    public boolean IsDeleted() {
        return IsDeleted;
    }
    
    public boolean IsFiltered()
    {
        return IsFiltered;
    }
    
    public void SetDeleteFlag(boolean flag) {
        this.IsDeleted = flag;
    }
    
    public void SetFilterFlag(boolean flag)
    {
        this.IsFiltered = flag;
    }

    public int getSig_Id() {
        return sig_Id;
    }

    public void setSig_Id(int sig_Id) {
        this.sig_Id = sig_Id;
    }

    public int getColumnNo() {
        return columnNo;
    }
    
    public String getSig_name() {
        return name;
    }

    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }
    
    public int getNemberofSections()
    {
        return this.sections.size();
    }

    public int getSelectedIndexByPlot() {
        if(this.IsIndexIn_Signal)
        {
            return selectedIndex;
        }
        else
        {
            double time = this.sections.get(this.selectedSectionID).section_time.get(selectedIndex);
            int IndexInSignal = Utils.findTimeIndexFrom(time, sig_time);
            return IndexInSignal;
        }
    }
    
    public double getSignalIntegral(){
        
        double integral = 0.0;
        for(int i=0; i< this.sig_value.size()-1;i++)
        {
            integral = integral + ((this.sig_value.get(i)+this.sig_value.get(i+1))/2)*this.sig_time.get(i);
        }
        return integral;
    }
    
   
    
    public void setSelectedSectionAndIndex(int SecID, int SelectedIndex,boolean IsIndexIn_Signal, boolean IsIndexIn_Peaks,boolean IsIndexIn_Nadirs)
    {
        this.selectedSectionID = SecID;
        this.selectedIndex = SelectedIndex;
        this.IsIndexIn_Peaks = IsIndexIn_Peaks;
        this.IsIndexIn_Nadirs = IsIndexIn_Nadirs;
        this.IsIndexIn_Signal = IsIndexIn_Signal;
    }
    
    public boolean IsPeakComputedForAllSections()
    {
        boolean check = true;
        for(Sections sec:this.sections)
        {
            check = check && sec.IsPeakFound;
        }
        return check;
    }
        
    public Signal(int sig_Id, int columnNo, String name) {
        this.sig_Id = sig_Id;
        this.columnNo = columnNo;
        this.name = name;
        
        this.ZoomedMaxX = 0.0;
        this.ZoomedMaxY = 0.0;
        this.ZoomedMinX = 0.0;
        this.ZoomedMinY = 0.0;
        this.PlotIsZoomed = false;
        
        this.IsDeleted = false;
        this.SignalHasOneSection = true;
        this.IsFiltered = false;
        
        this.Origin_sig_value = new ArrayList<>();
        this.Origin_sig_time = new ArrayList<>();
        this.sections = new ArrayList<>();
        
        this.IsIndexIn_Peaks = false; 
        this.IsIndexIn_Nadirs = false;
        this.IsIndexIn_Signal = false;
        
        this.IsPeakFound = false; 
        this.IsNormalized = false;
        this.IsNormalized_temp = false;
        this.IsBackgroundRemoved_temp = false;
        this.IsFitParamComputed = false;
        this.FittedCurve_Y = null;
        this.SigBGRemoved = false;
        this.IsAverageBaseLineComputed = false;
        this.AverageBaseLine = 0;
        this.IsSpikeBaseLinesComputed=false;
        this.FittedCurve_Y = null;
    }
    
    private int[] findSelectedIndexOnSections()
    {
        int IndexIn_PeakIndexMax_PeakIndexMin = -1;
        int SectionID = -1;
        int Index_InSection = -1;
        //selected point is a member of peakIndex_Max or peakIndex_Min 
        //arrays and we want to remove selected point from minpeakarray or maxpeak array 
        if (!this.IsIndexIn_Signal)
        {
            if(this.IsIndexIn_Peaks){ //selected point is member of peaks
                IndexIn_PeakIndexMax_PeakIndexMin = this.sections.get(this.selectedSectionID).peakIndex_Max.indexOf(selectedIndex);                
            }
            else if (this.IsIndexIn_Nadirs){ //selected point is member of nadirs
                IndexIn_PeakIndexMax_PeakIndexMin = this.sections.get(this.selectedSectionID).peakIndex_Min.indexOf(selectedIndex);
            }
            SectionID = this.selectedSectionID;
            Index_InSection = selectedIndex;
        } 
        //selected point is member of signal array and we want to add this point
        //to Maxpeak array or MinPeak array
        else {
            if (this.SignalHasOneSection)//Signal has one section
            {               
                SectionID = 0;
                Index_InSection = selectedIndex;
            } 
            else//Signal has several section and we should find in which section it is located
            {            
                int IndexOfSelectedValue = -1;                
                for (int i = 0; i < this.sections.size(); i++) {                      

                    IndexOfSelectedValue = this.sections.get(i).section_time.indexOf(this.sig_time.get(selectedIndex));

                    if (IndexOfSelectedValue != -1) {
                        SectionID = i;
                        Index_InSection = IndexOfSelectedValue;
                        break;
                    }
                }               
            }
        }
        int[] PeakorNadir_Index = {SectionID, Index_InSection, IndexIn_PeakIndexMax_PeakIndexMin};
        return PeakorNadir_Index;
    }
     
    public boolean addPeak(Experiment exp)
    {
        int[] PeakorNadir_Index = findSelectedIndexOnSections();
        int SectionId = PeakorNadir_Index[0];        
        int IndexInSection = PeakorNadir_Index[1];                
        int IndexInPeakIndexMax = PeakorNadir_Index[2];        
        if(SectionId != -1 && IndexInSection!=-1)
        {
            if(IndexInPeakIndexMax == -1) // this point isn't added to peaks before and we can add it now
            {
               this.sections.get(SectionId).addPeak(IndexInSection);
               return true;
            }
            else
                return false;
        }
        else
            return false;
    }
    public boolean addNadir(Experiment exp){
        int[] PeakorNadir_Index = findSelectedIndexOnSections();
        int SectionId = PeakorNadir_Index[0];        
        int IndexInSection = PeakorNadir_Index[1];                
        int IndexInPeakIndexMin = PeakorNadir_Index[2];        
        if(SectionId != -1 && IndexInSection!=-1)
        {
            if(IndexInPeakIndexMin == -1) // this point isn't added to Nadirs before and we can add it now
            {
               this.sections.get(SectionId).addNadir(IndexInSection);
               return true;
            }
            else
                return false;
        }
        else
            return false;
    }
    
    public boolean removePeakorNadir(Experiment exp)
    {
        int[] PeakorNadir_Index = findSelectedIndexOnSections();
        int SectionId = PeakorNadir_Index[0];
        int IndexIn_PeakIndexMax_PeakIndexMin = PeakorNadir_Index[2];
        if (SectionId != -1) {
            if(IndexIn_PeakIndexMax_PeakIndexMin != -1) // selected point is a Peak
            {
                this.sections.get(SectionId).removePeakandNadir(IndexIn_PeakIndexMax_PeakIndexMin,this.IsIndexIn_Peaks,this.IsIndexIn_Nadirs);               
                return true;
            } else 
            {
                return false;
            }
        } 
        else 
        {
            return false;
        }
    }
  
    public void AddMultiSection(int numberOfSections, ArrayList<String> SectionNames) {
        
        this.NumberOfSections = numberOfSections;
        this.NameOfSections = SectionNames;
        for (int i = 0; i < this.NumberOfSections; i++) {
            Sections sec = new Sections(this.NameOfSections.get(i), i,this.sig_Id);
            this.sections.add(sec);  //section Id is the same as index os section in sections array.          
        }
        this.SignalHasOneSection = false;        
    }
    
    public void AddMultiSection() {
        
        for (int i = 0; i < this.NumberOfSections; i++) {
            Sections sec = new Sections(this.NameOfSections.get(i), i,this.sig_Id);
            this.sections.add(sec);            
        }
        this.SignalHasOneSection = false;        
    }
    
    public void AddOneSection() {        
        
        this.NumberOfSections = 1; // whole signal is one section
        this.NameOfSections = null;
        Sections sec = new Sections("Default", 0,this.sig_Id);
        sec.section_time = this.sig_time;
        sec.section_value = this.sig_value;
        sec.startIndex_InSignal = 0;
        sec.EndIndex_InSignal = this.sig_time.size()-1;
        this.sections.add(sec);
        sec.TimeIsAddedToSection = true;
        this.SignalHasOneSection = true;         
    }
    
    public boolean IsTimeAddedToAllSections()
    {
       boolean check = true;
       for (Sections sec : this.sections) {
           check = check && sec.TimeIsAddedToSection;
       } 
       return check;
    }
   
    public boolean setPeakPercent(double PeakPercent,int SectionId)
    {
        if(this.sections.get(SectionId).TimeIsAddedToSection)
        {
           this.sections.get(SectionId).setPeakPercent(PeakPercent);
           return true;
        }
        else
            return false;
    }
    
    public int getPeaknumber(){
        int tot_peaks = 0;
        for(Sections sec: this.sections)
        {
            tot_peaks += sec.getPeakNumber();
        }
        return tot_peaks;
    }
    
    public int findPeaks(double PeakPercent,int SectionId)
    {
        if(this.setPeakPercent(PeakPercent,SectionId))
        {
            //this.sections.get(SectionId).findPeaks();
            this.IsPeakFound = this.IsPeakFound | this.sections.get(SectionId).findPeaks();  
//            if(this.IsPeakFound && Normalize && !this.IsNormalized)
//            {
//                this.NormalizeSignal();              
//            }
        }        
        return getPeaknumber();
    }
    
    //IsPeakFound is true when at least peaks are found for one section
    public boolean setPeakPercent(double PeakPercent)
    {
        if(this.sections.get(0).TimeIsAddedToSection)
        {
           this.sections.get(0).setPeakPercent(PeakPercent);
           return true;
        }
        else
            return false;
    }
    
//    public int findPeaks(double PeakPercent, boolean Normalize)
//    {
//        this.PeakPercent = PeakPercent;
//        if(this.setPeakPercent(PeakPercent))
//        {
//            this.IsPeakFound = this.sections.get(0).findPeaks();
////            if(this.IsPeakFound && Normalize && !this.IsNormalized)
////            {
////                this.NormalizeSignal();              
////            }
//        }
//        return getPeaknumber();
//    }    
    public int findPeaks(double PeakPercent)
    {
        if(this.setPeakPercent(PeakPercent))
        {
            this.IsPeakFound = this.sections.get(0).findPeaks();
            
        }
        return getPeaknumber();
    }
       
    public void CopyInitialSignal() {
        sig_time = null;
        sig_value = null;

        sig_time = new ArrayList<>();
        sig_value = new ArrayList<>();
        for (int i = 0; i < this.Origin_sig_time.size(); i++) {
            sig_time.add(Origin_sig_time.get(i));
            sig_value.add(Origin_sig_value.get(i));
        }
    }

//    public boolean NormalizeSignal() {
//        if(!this.IsNormalized && this.IsPeakFound)
//        {            
//            double sumSignal = 0;
//            double AvergeBaseLine = 0;
//            int count =0;            
//            int IndexOfFirstSectionInSignal = 0;
//            
//            double minTime = this.sections.get(0).section_time.get(0);
//            for(int i=1; i<this.sections.size();i++){
//                if(minTime > this.sections.get(i).section_time.get(0))
//                {
//                    minTime = this.sections.get(i).section_time.get(0);
//                    IndexOfFirstSectionInSignal = i;
//                }
//            }
//            if(this.sections.get(IndexOfFirstSectionInSignal).IsPeakFound){
//                for(int i=0; i<this.sections.get(IndexOfFirstSectionInSignal).peakIndex_Min.get(0);i++)
//                {
//                    sumSignal += this.Origin_sig_value.get(i);
//                    count++;
//                }
//                if(count > 0)
//                {
//                    AvergeBaseLine = sumSignal/count;
//                    for (int i = 0; i < this.sig_time.size(); i++) {
//
//                        this.sig_value.set(i, this.sig_value.get(i)/AvergeBaseLine);                                       
//                    }
//                    this.IsNormalized = true;
//                    this.EditSignal(-1.0, -1.0);
//                    findPeaks();
//                    return true;
//                }
//                else
//                    return false;
//            }
//            else
//            {
//                return false;
//            }              
//        }
//        else
//            return false;
//
//    }
    
    
    
    public void FastFourierTransform()
    {        
        //double [][] freMag = FFT.getMag(x,Fs);
//        double [] freq = new double[freMag.length];
//        double[] Mag = new double[freMag.length];
//        for(int i=0; i< freMag.length ; i++)
//        {
//            freq[i] = freMag[i][0];
//            Mag[i] = freMag[i][1];
//        }
        
    }

   public boolean EditSignalWithOriginalSignal(double timeFrom, double timeTo) {
        
//        if(timeFrom == -1 && timeTo == -1)
//        {
//            if (normalizeSignal) {
//                this.NormalizeSignal();
//                this.IsNormalized = true;
//                 EditSectionsByEditingSignlTime(this.sig_time.get(0),this.sig_time.get(this.sig_time.size()-1));
//                 this.IsPeakFound = false;
//            }
//            else
//            {
//                this.IsNormalized = false;
//            }
//            return true;
//        }
//        else
//        {
            if(timeFrom == -1)
            {
               timeFrom = this.sig_time.get(0);
            }
            if(timeFrom < Utils.findMin(this.Origin_sig_time))
            {
                timeFrom = Utils.findMin(this.Origin_sig_time);
            }
            if(timeTo == -1)
            {
                timeTo = this.sig_time.get(this.sig_time.size()-1);
            }

            if(timeTo > Utils.findMax(this.Origin_sig_time))
            {
                timeTo = Utils.findMax(this.Origin_sig_time);
            }

            // just get the inetger part of time
            double T1 = Double.parseDouble(Utils.DoubleToStringEmitFloats(timeFrom));
            double T2 = Double.parseDouble(Utils.DoubleToStringEmitFloats(timeTo));


            int TimeIndex_From = Utils.findTimeIndexFrom(T1, this.Origin_sig_time);
            int TimeIndexTo = Utils.findTimeIndexTo(T2, this.Origin_sig_time);
            if (TimeIndex_From == -1 || TimeIndexTo == -1) {
                return false;
            } else {
                if (TimeIndex_From >= TimeIndexTo) {
                    return false;
                } else {
                    this.sig_time.clear();
                    this.sig_value.clear();

                    for (int i = TimeIndex_From; i <= TimeIndexTo; i++) {
                        this.sig_time.add(this.Origin_sig_time.get(i));
                        this.sig_value.add(this.Origin_sig_value.get(i));
                    }
//                    if (normalizeSignal) {
//                        this.NormalizeSignal();
//                        this.IsNormalized = true;
//                    }
//                    else
//                    {
//                        this.IsNormalized = false;
//
//                    }
                    EditSectionsByEditingSignlTime(T1,T2);// whenever signal is edited, section time and section velues should be edit.

                    this.IsPeakFound = false;
                    //this.IsPeakShownForAllSections = false;
                    return true;
                }
            }
        //}
    }
    
    public boolean EditSignal(double timeFrom, double timeTo) {
        
        if(timeFrom == -1 && timeTo == -1)
        {           
            EditSectionsByEditingSignlTime(this.sig_time.get(0),this.sig_time.get(this.sig_time.size()-1));
            this.IsPeakFound = false;
            return true;
        }
        else
        {
            if(timeFrom == -1)
            {
               timeFrom = this.sig_time.get(0);
            }
            if(timeFrom < Utils.findMin(this.sig_time))
            {
                timeFrom = Utils.findMin(this.sig_time);
            }
            if(timeTo == -1)
            {
                timeTo = this.sig_time.get(this.sig_time.size()-1);
            }

            if(timeTo > Utils.findMax(this.sig_time))
            {
                timeTo = Utils.findMax(this.sig_time);
            }

            // just get the inetger part of time
            double T1 = Double.parseDouble(Utils.DoubleToStringEmitFloats(timeFrom));
            double T2 = Double.parseDouble(Utils.DoubleToStringEmitFloats(timeTo));


            int TimeIndex_From = Utils.findTimeIndexFrom(T1, this.sig_time);
            int TimeIndexTo = Utils.findTimeIndexTo(T2, this.sig_time);
            if (TimeIndex_From == -1 || TimeIndexTo == -1) {
                return false;
            } else {
                if (TimeIndex_From >= TimeIndexTo) {
                    return false;
                } else {
                    ArrayList<Double> sig_time_tmp = (ArrayList<Double>)this.sig_time.clone();
                    ArrayList<Double> sig_value_tmp = (ArrayList<Double>)this.sig_value.clone();
                    this.sig_time.clear();
                    this.sig_value.clear();

                    for (int i = TimeIndex_From; i <= TimeIndexTo; i++) {
                        this.sig_time.add(sig_time_tmp.get(i));
                        this.sig_value.add(sig_value_tmp.get(i));
                    }
                    sig_time_tmp = null;
                    sig_value_tmp = null;
                    
                    EditSectionsByEditingSignlTime(T1,T2);// whenever signal is edited, section time and section velues should be edit.

                    this.IsPeakFound = false;
                    //this.IsPeakShownForAllSections = false;
                    return true;
                }
            }
        }
    }
 
    public boolean EditSectionValuesByChangingSignalValues()
    {
        boolean AllSectionAreEditted = true;
        for (Sections sec : this.sections) { 
                        
            AllSectionAreEditted = AllSectionAreEditted && sec.EditSectionValues(this);            
        }
        
        if(AllSectionAreEditted)
        {
           this.SigBGRemoved = true;
           this.IsNormalized = true;
//           this.IsPeakFound = false;           
//           this.sig_MaxPeak_Index = null;            
        }
        return AllSectionAreEditted;       
    }
    
    private void EditSectionsByEditingSignlTime(double timeFrom, double timeTo)
    {       
        for (Sections sec : this.sections) { 
            if(sec.TimeIsAddedToSection)
            {
                double sectionTimeFrom = sec.section_time.get(0);
                double sectionTimeTo = sec.section_time.get(sec.section_time.size()-1);
                if(sectionTimeFrom < timeFrom)
                {
                    sectionTimeFrom = timeFrom;
                }
                
                if(sectionTimeTo > timeTo)
                {
                    sectionTimeTo = timeTo;
                }
               this.AddTimeAndValueToSections(sectionTimeFrom, sectionTimeTo, sec.getSecID());                
            }
        }         
    }
    
    public boolean AddTimeAndValueToSections(double timeFrom, double timeTo, int SectionID) {
       
        int TimeIndex_From = Utils.findTimeIndexFrom(timeFrom, this.sig_time);
        int TimeIndexTo = Utils.findTimeIndexTo(timeTo, this.sig_time);
        if (TimeIndex_From == -1 || TimeIndexTo == -1) {
            return false;
        } else {
            if (TimeIndex_From >= TimeIndexTo) {
                return false;
            } else {

                ArrayList<Double> TimeRange = new ArrayList<>();
                ArrayList<Double> Value = new ArrayList<>();
                for (int i = TimeIndex_From; i <= TimeIndexTo; i++) {

                    TimeRange.add(this.sig_time.get(i));
                    Value.add(this.sig_value.get(i));
                }

                boolean NoConfilict = true;
                for (Sections sec : this.sections) {
                    if (sec.getSecID() != SectionID) {
                        NoConfilict = (NoConfilict && sec.CheckIfTwoSectionsHaveConfilict(TimeRange));
                    }
                }
                
                if (NoConfilict) {
                    this.sections.get(SectionID).AddTimeAndValue(TimeRange, Value); 
                    this.sections.get(SectionID).startIndex_InSignal = TimeIndex_From;
                    this.sections.get(SectionID).EndIndex_InSignal = TimeIndexTo;
                    //this.IsPeakShownForAllSections = false;
                    
                    return NoConfilict;
                } else {
                    return NoConfilict;
                }
            }
        }        
    }
    
    public void EditSignalTimeFactor(float preFactor, float newFactor){
        
        for(int i=0; i<this.Origin_sig_time.size();i++){
            double newTime = (this.Origin_sig_time.get(i)/preFactor)*newFactor;
            this.Origin_sig_time.set(i, newTime);     
        }
        
        for(int i=0; i< this.sig_time.size();i++)
        {
            double newTime = (this.sig_time.get(i)/preFactor)*newFactor;
            this.sig_time.set(i, newTime);            
        }
        for (Sections sec : this.sections){
            if(sec.TimeIsAddedToSection){
                int secIndex = 0;
                for(int j=sec.startIndex_InSignal;j<=sec.EndIndex_InSignal; j++){                
                    sec.section_time.set(secIndex, this.sig_time.get(j));
                    secIndex++;
                }
            }        
        }
        for(Sections sec : this.sections){
            if(sec.IsPeakFound)
            {
                sec.computeISI();
                sec.ComputeSpikeWidthAndAMP2();
            }
        }
    }

    public void ResetSignal() {
        this.sig_time.clear();
        this.sig_value.clear();
        for (int i = 0; i < this.Origin_sig_time.size(); i++) {
            this.sig_time.add(Origin_sig_time.get(i));
            this.sig_value.add(this.Origin_sig_value.get(i));
        }       
        this.sections.clear();
        if(this.SignalHasOneSection)
        {            
            this.AddOneSection();
        }
        else
        {
            this.AddMultiSection();
        }
        this.IsPeakFound = false;
        //this.IsPeakShownForAllSections = false;
        this.SigBGRemoved = false;
        this.IsNormalized = false;
        this.resetBackgroundFlags();
       
    }

    public boolean createPeakPlot(Experiment exp,SignalPlot sigPlot)
    {      
        //boolean check = true;   
        boolean IsPeakFound_tmp = false;
        for (Sections sec : this.sections)
        {
            if(sec.IsPeakFound)
            {
               sigPlot.getSignalsWithMaxPeaks(sec,true); 
            }
            else
            {
                sigPlot.removePeakPlot(sec);
            }
            IsPeakFound_tmp = IsPeakFound_tmp || sec.IsPeakFound ;
            //when a section had peaks before and its peaks are shown on the plot,
            //but with new value of peak threshold, this section does not have peak any more.
            //in this case we should remove the peak plot related to this section.
            //check = check && sec.IsPeakFound;
        } 
        this.IsPeakFound = IsPeakFound_tmp; 
        return this.IsPeakFound;        
    }
     
    public SignalPlot createSignalPlot(Experiment exp) {
        
        if (this.sig_time.size() == this.sig_value.size()) {

            SignalPlot sigPlot = new SignalPlot(this,exp);  
            for (Sections sec : this.sections) {
                this.AddSectionsToSignalPlot(sec.getSecID(),sigPlot);
            }
            if(this.IsPeakFound)
            {
               this.createPeakPlot(exp, sigPlot);
            }
           
            return sigPlot;
        }
        else
            return null;
        
    }
    
    public SignalPlot createSignalPlotWithoutPeaksAndSections(Experiment exp) {
        
        if (this.sig_time.size() == this.sig_value.size()) {
  
            return  new SignalPlot(this,exp);
        }
        else
            return null;
        
    }

    public void AddSectionsToSignalPlot(int SectionID, SignalPlot sigPlot) {

        if(sigPlot!=null)
        {
            if (!this.SignalHasOneSection) {

                if (this.sections.get(SectionID).TimeIsAddedToSection) {
                    double[] x1 = new double[2];
                    double[] y1 = new double[2];
                    double[] x2 = new double[2];
                    double[] y2 = new double[2];
                    int count = 0;
                    x1[count] = Utils.findMin(this.sections.get(SectionID).section_time);
                    y1[count] = Utils.findMin(this.sections.get(SectionID).section_value);

                    x2[count] = Utils.findMax(this.sections.get(SectionID).section_time);
                    y2[count] = Utils.findMin(this.sections.get(SectionID).section_value);
                    count++;

                    x1[count] = Utils.findMin(this.sections.get(SectionID).section_time);
                    y1[count] = Utils.findMax(this.sections.get(SectionID).section_value);

                    x2[count] = Utils.findMax(this.sections.get(SectionID).section_time);
                    y2[count] = Utils.findMax(this.sections.get(SectionID).section_value);

                    sigPlot.AddSectionLines(x1, y1, x2, y2, SectionID,this);
                    x1 = null;
                    y1 = null;
                    x2 = null;
                    y2 = null;
                    System.gc();
                }            
            }
        }
    }
    
    public PlotPoints CreateISITimePlot()
    {    
        //if at least one section of signal has peak, IsPeakFound variable of signal has true value
        //and signal shoula have more than one peak
        if(this.IsPeakFound && this.getPeaknumber()>1)
        {
            PlotPoints ISI_TimePlot = new PlotPoints(); 
            double MinISI = Math.pow(10,8);
            double MaxISI = 0;
          
            boolean IsThereISI = false;
            for (Sections section : this.sections) {
                if (section.IsPeakFound && section.computeAndGetISIs()!=null) {

                    if(Utils.findMin(section.getISIs())<MinISI)
                    {
                       MinISI = Utils.findMin(section.getISIs()); 
                    }
                    if(Utils.findMax(section.getISIs())>MaxISI)
                    {
                        MaxISI = Utils.findMax(section.getISIs());
                    }
                    String ComponentID = "ISI-Time" + String.valueOf(section.getSecID());
   
                    ISI_TimePlot.AddPlot(null, section.getPeakTime(), section.getISIs(), ComponentID, 
                            Utils.findMin(sig_time),MinISI,Utils.findMax(sig_time),
                            MaxISI, "Time (s)", "ISI (s)",false,false);
                   
                    IsThereISI = true;
                }               
            } 
            if(IsThereISI)
                return ISI_TimePlot; 
            else
                return null;
        }
        else
            return null;
    }
   
    public double getStartTime()
    {
        return this.sig_time.get(0);
    }
    
    public double getEndTime()
    {
        return this.sig_time.get(this.sig_time.size()-1);
    }
    
    public void locateSectionMaxPeaksInSignalArray()
    {
        this.sig_MaxPeak_Index = null;
        if(this.IsPeakComputedForAllSections())
        {            
            this.sig_MaxPeak_Index = new ArrayList();

            for(int j=0 ; j< this.NumberOfSections; j++)
            {
                Sections sec = this.sections.get(j);
                for(int i=0; i<sec.peakIndex_Max.size(); i++)
                {
                    this.sig_MaxPeak_Index.add(sec.startIndex_InSignal+sec.peakIndex_Max.get(i));
                }
            }
            Collections.sort(this.sig_MaxPeak_Index);
        }
    }
 
    public int[] findMinimumPeaksForCurveFitting()
    {       
       //define a window between each two peaks, and find global minimum in this window.
        //also the first point of signal should added.
       this.locateSectionMaxPeaksInSignalArray();
       
       int[] IndexInSignal_min = new int[this.sig_MaxPeak_Index.size()+1];
       if(this.sig_MaxPeak_Index.size()>0)
       {                 
            int k =0; 
            //first minimum point in the signal before first peak
            int StartIndex = 0;
            int EndIndex = this.sig_MaxPeak_Index.get(0);
            double minValue = this.sig_value.get(0);
           
            for(int i=StartIndex; i< EndIndex; i++)                
            {
                if(this.sig_value.get(i) < minValue)
                {
                    minValue = this.sig_value.get(i);
                    IndexInSignal_min[k] = i;
                }
            }
            
            k++;            
            for(int i=0; i<this.sig_MaxPeak_Index.size()-1; i++)
            {
                StartIndex = this.sig_MaxPeak_Index.get(i);
                EndIndex = this.sig_MaxPeak_Index.get(i+1);
                minValue = this.sig_value.get(StartIndex);
                
                for(int j=StartIndex+1 ; j<EndIndex; j++ )
                {
                    if(this.sig_value.get(j) < minValue)
                    {
                        minValue = this.sig_value.get(j);
                        IndexInSignal_min[k] = j;
                    }                    
                }
            k++;                
            }
            
            StartIndex = this.sig_MaxPeak_Index.get(this.sig_MaxPeak_Index.size()-1);
            EndIndex = this.sig_time.size() -1;
            minValue = this.sig_value.get(StartIndex);
            
            for(int i=StartIndex; i<EndIndex; i++ )
            {
                if(this.sig_value.get(i) < minValue)
                {
                    minValue = this.sig_value.get(i);
                    IndexInSignal_min[k] = i;
                }
            }           
       }
       return IndexInSignal_min;  
    }
    //tis function is executed when the value of comnboBox is changed
    public boolean ComputeRegressionParams(EnumType.CurveType selectedCurve, SignalPlot sig_plot)
    {   
        this.IsFitParamComputed = false;
        this.FittedParamToBackground = null;
        
        int[] IndexInSignal_min = findMinimumPeaksForCurveFitting();
        double[] min_value = new double[IndexInSignal_min.length]; 
        double[] min_time = new double[IndexInSignal_min.length]; 
        for(int i=0; i<IndexInSignal_min.length; i++)
        {
            min_time[i] = this.sig_time.get(IndexInSignal_min[i]);
            min_value[i] = this.sig_value.get(IndexInSignal_min[i]);
        }
        // should find minimum of minimums
        
        boolean IsThereEnoughData = false;
         //number of data points should be more than 1+ degree of polynomial
        int curveDegree = 0;
        switch(selectedCurve)
        {
            case LINEAR:
            {
                if(IndexInSignal_min.length > 2)
                {
                    IsThereEnoughData = true;
                    curveDegree = 1;
                }
                break;
            }
            case POLY_DEGREE2:
            {
                if(IndexInSignal_min.length > 3)
                {
                    IsThereEnoughData = true;
                    curveDegree = 2;
                }
                break;
            }
            case POLY_DEGREE3:
            {
                if(IndexInSignal_min.length > 4)
                {
                    IsThereEnoughData = true;
                    curveDegree = 3;
                }
                break;
            }
            case POLY_DEGREE4:
            {
                if(IndexInSignal_min.length > 5)
                {
                    IsThereEnoughData = true;
                    curveDegree = 4;
                }
                break;
            }
            case POLY_DEGREE_N: 
            {
                //N is equal to the number of points in the dataset
                curveDegree = IndexInSignal_min.length-1;
                IsThereEnoughData = true;
                
                break;
            }
        }
        if(IsThereEnoughData)
        {            
           this.FittedParamToBackground = sig_plot.ComputePolyRegressionParameters(min_time, min_value,curveDegree);
//            this.FittedParamToBackground = sig_plot.ComputePolyRegressionParameters
//        (Utils.convertDoubleArrayListToArray(sig_time),Utils.convertDoubleArrayListToArray(this.sig_value),curveDegree);
            this.FittedCurve = selectedCurve;
            this.IsFitParamComputed = true;
        }
        IndexInSignal_min = null;
        min_value = null;
        min_time = null;
        return IsFitParamComputed;        
    }
    
    public void FitCurve(EnumType.CurveType selectedCurve, SignalPlot sig_plot)
    {
        if(this.ComputeRegressionParams(selectedCurve, sig_plot))
        {
            sig_plot.AddNewPlotToSignalPlot(SignalPlot.ComponentIDs.SolidLine, 
                            Utils.convertDoubleArrayListToArray(this.sig_time), this.getFittedCurve_Y());
        }
    }
    
    private double getSigBaseLineBeforeFirstResponse()
    {
        double sumSignal = 0;
        int count = 0;
         
        int IndexOfFirstSectionInSignal = 0;
        double minTime = this.sections.get(0).section_time.get(0);
        //find index of section that contains the initial part of signal        
        for (int i = 1; i < this.sections.size(); i++) {
            if (minTime > this.sections.get(i).section_time.get(0)) {
                minTime = this.sections.get(i).section_time.get(0);
                IndexOfFirstSectionInSignal = i;
            }
        }
        if (this.sections.get(IndexOfFirstSectionInSignal).IsPeakFound) {
            //get the average of signal intensity in this region
            for (int i = 0; i < this.sections.get(IndexOfFirstSectionInSignal).peakIndex_Min.get(0); i++) {
                sumSignal += this.sections.get(IndexOfFirstSectionInSignal).section_value.get(i);
                count++;
            }
        }
        else
        {
            //if no peak is founded
            for (int i = 0; i < this.sections.get(IndexOfFirstSectionInSignal).section_value.size(); i++) {
                sumSignal += this.sections.get(IndexOfFirstSectionInSignal).section_value.get(i);
                count++;
            }
        } 
        if (count > 0) {
            return sumSignal / count;
        }
        else
            return -1;
    }
    
    // computing the average intensity of signal before first peak happening
    public void ConstantBaseLine(SignalPlot sig_plot) {
        
        this.AverageBaseLine = 0; 
        this.IsAverageBaseLineComputed = false;
        double[] x_time = new double[2];
        x_time[0]= this.sig_time.get(0);
        x_time[1] = this.sig_time.get(this.sig_time.size()-1);                 
        double[] y_baseLine = new double[2];        
        double AvergeBaseLine = getSigBaseLineBeforeFirstResponse();
        if(AvergeBaseLine>0)
        {
            y_baseLine[0] = AvergeBaseLine;
            y_baseLine[1] = AvergeBaseLine;
            sig_plot.AddNewPlotToSignalPlot(SignalPlot.ComponentIDs.SolidLine, x_time, y_baseLine);
            this.IsAverageBaseLineComputed = true;
            this.AverageBaseLine = AvergeBaseLine;
            // when signal is fitted to constant value, following variables should reset
            this.IsFitParamComputed = false;
            this.IsSpikeBaseLinesComputed  = false; 
        }
    }
    
    public void FitSpikeBaseLine(SignalPlot sig_plot)
    {
        this.Y_Spikebasline = null;
        this.IsSpikeBaseLinesComputed = false;
        
        int[] IndexInSignal_min = findMinimumPeaksForCurveFitting();
        double[] min_value = new double[IndexInSignal_min.length]; 
        double[] min_time = new double[IndexInSignal_min.length]; 
        for(int i=0; i<IndexInSignal_min.length; i++)
        {
            min_time[i] = this.sig_time.get(IndexInSignal_min[i]);
            min_value[i] = this.sig_value.get(IndexInSignal_min[i]);
        }
        //compute tha equation of the line that connet two consecuitve point of min_data
        //if we have n min data, we should compute the slope and the intercept of (n-1) equation
        double[] slope = new double[IndexInSignal_min.length-1]; 
        double[] intercept=new double[IndexInSignal_min.length-1];
        
        //If we have n minimum points, (n-1) equation is computed.
        for(int i=0; i<IndexInSignal_min.length-1; i++)
        {
           slope[i]=(min_value[i+1]- min_value[i])/(min_time[i+1]-min_time[i]); 
           
           intercept[i]= min_value[i] - slope[i]*min_time[i];
        }
        
        //compute fitted Y values for each equation
        this.Y_Spikebasline = new double[this.sig_time.size()];
        
        for(int i=0; i<IndexInSignal_min[0];i++)
        {
            Y_Spikebasline[i]= this.sig_value.get(IndexInSignal_min[0]);
        }
        
        for(int i=0; i<IndexInSignal_min.length-1;i++)
        {
            for(int j=IndexInSignal_min[i]; j<IndexInSignal_min[i+1];j++)
            {
               Y_Spikebasline[j] = slope[i]*this.sig_time.get(j)+intercept[i];
               
            }
        }
        
        for(int i=IndexInSignal_min[IndexInSignal_min.length-1]; i< this.sig_time.size();i++)
        {
            Y_Spikebasline[i] = this.sig_value.get(IndexInSignal_min[IndexInSignal_min.length-1]);
        }
        if(Y_Spikebasline.length == this.sig_time.size())
        {
            sig_plot.AddNewPlotToSignalPlot(SignalPlot.ComponentIDs.SolidLine, 
                Utils.convertDoubleArrayListToArray(sig_time), Y_Spikebasline);
            this.IsSpikeBaseLinesComputed = true;
            this.IsFitParamComputed = false;
            this.IsAverageBaseLineComputed = false;
        }             
    }
        
    private void ComputeFittedCurve_Y()
    {
        if(this.IsFitParamComputed)
        {
            this.FittedCurve_Y = null;
            this.FittedCurve_Y = new double[this.sig_time.size()];
            
            for(int i=0; i<this.sig_time.size();i++ )
            {
                this.FittedCurve_Y[i] = 0;
                for(int j=0; j<this.FittedParamToBackground.length-1;j++)
                {
                    this.FittedCurve_Y[i] = this.FittedCurve_Y[i] + 
                            this.FittedParamToBackground[j]*Math.pow(sig_time.get(i),j);
                }
            }
            this.IsAverageBaseLineComputed = false;
            this.IsSpikeBaseLinesComputed  = false;            
        }
    }

   public boolean subtractBackgrund()
   {
       if(!this.SigBGRemoved) 
       {
            if(this.IsFitParamComputed)
            {
                ComputeFittedCurve_Y();
                if(this.FittedCurve_Y != null)
                {
                    for(int i=0; i<this.sig_value.size(); i++)
                    {
                        double newSigValue = this.sig_value.get(i) - this.FittedCurve_Y[i];
                        this.sig_value.set(i, newSigValue);
                    }
                    
                    this.IsBackgroundRemoved_temp = true;
                }
            }
            else{    
                if(this.IsAverageBaseLineComputed)
                {
                    for(int i=0; i<this.sig_value.size(); i++)
                    {
                        double newSigValue = this.sig_value.get(i) - this.AverageBaseLine;
                        this.sig_value.set(i, newSigValue);
                    }
                    this.IsBackgroundRemoved_temp = true;
                }
                else
                {
                    if( this.IsSpikeBaseLinesComputed && this.Y_Spikebasline!=null)
                    {
                        for(int i=0; i<this.sig_value.size(); i++)
                        {
                            double newSigValue = this.sig_value.get(i) - this.Y_Spikebasline[i];
                            this.sig_value.set(i, newSigValue);
                        }
                        
                        this.IsBackgroundRemoved_temp = true;
                    }
                }
            }
            
            if(this.IsBackgroundRemoved_temp) // ignore the negative values in the signal
            {
                double min = Utils.findMin(this.sig_value);
                if(min<0)
                {
                    double minimum = Math.abs(min);
                    for(int i=0; i<this.sig_value.size(); i++){
                        double newSigValue = this.sig_value.get(i)+minimum;
                        this.sig_value.set(i, newSigValue);
                    }
                }
            }
            
       }
       
       return this.IsBackgroundRemoved_temp;
    }  
   
   public void resetBackgroundFlags()
   {
       this.IsFitParamComputed = false;
       this.IsBackgroundRemoved_temp = false;
       this.FittedCurve_Y = null;
       this.FittedParamToBackground = null;
       this.FittedCurve = EnumType.CurveType.None;
       this.IsAverageBaseLineComputed = false;
       this.IsNormalized_temp = false;
       this.IsAverageBaseLineComputed=false;
       this.Y_Spikebasline = null;
   }
   
   public boolean Normalize()
   {      
       if(!this.IsNormalized)
       {
            if(this.IsFitParamComputed) 
            {
                if(this.FittedCurve_Y != null)
                {
                    //double baseline = this.getSigBaseLineBeforeFirstResponse();
                    for(int i=0; i<this.sig_value.size(); i++)
                    {
                        if(this.FittedCurve_Y[i]>0)
                        {                            
                            double newSigValue = (this.sig_value.get(i))/(this.FittedCurve_Y[i]);
                            this.sig_value.set(i, newSigValue);
                        }
                        else
                        {
                            System.out.println("There is zero value or negative vaule");
                            this.sig_value.set(i, 0.0);       
                        }
                    } 
                    
                                       
                    this.IsNormalized_temp = true;
                 }
            }
            else
            {
                if(this.IsAverageBaseLineComputed && this.AverageBaseLine!=0)
                {
                    for(int i=0; i<this.sig_value.size(); i++){
                        double newSigValue = this.sig_value.get(i)/this.AverageBaseLine;
                        this.sig_value.set(i, newSigValue);
                    }
                    double baseline = Math.abs(Utils.findMin(this.sig_value));
                    for(int i=0; i<this.sig_value.size(); i++){
                        double newSigValue = this.sig_value.get(i)+baseline;
                        this.sig_value.set(i, newSigValue);
                    }
                    this.IsNormalized_temp = true;
                }
                else
                {
                    if(IsSpikeBaseLinesComputed && this.Y_Spikebasline!=null)
                    {                        
                        for(int i=0; i<this.sig_value.size(); i++){
                            double newSigValue = this.sig_value.get(i)/(Y_Spikebasline[i]) ;
                            //double newSigValue = this.sig_value.get(i)/this.AverageBaseLine;
                            this.sig_value.set(i, newSigValue);
                        }
                        double baseline = Math.abs(Utils.findMin(this.sig_value));
                        for(int i=0; i<this.sig_value.size(); i++){
                            double newSigValue = this.sig_value.get(i)+baseline;
                            this.sig_value.set(i, newSigValue);
                        }
                        this.IsNormalized_temp = true;
                    }
                }
            }
        }
       return this.IsNormalized_temp;
   }
    
}
