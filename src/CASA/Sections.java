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
/**
 *
 * @author mahsa.moein
 */
public class Sections {
    
    private int section_ID;   //starts from zero. the first defined section, has Id = 0.
    private String section_Name;
    
    public int startIndex_InSignal; //section start index in the signal
    public int EndIndex_InSignal; //section endindex in the signal
    
    //each calcium ion have 2 positive charge
    private static final double CALCIUM_CHARGE = 3.2e-19;
    private static final double AVOGADRO_CONSTANT = 6e+23;
    
    public ArrayList<Double> section_time; // the time array which we can edit it  
    public ArrayList<Double> section_value;
      
    public ArrayList<Integer> peakIndex_Max;
    public ArrayList<Integer> UserAddedPeak_Index;
    public ArrayList<Integer> peakIndex_Min;
    public ArrayList<Integer> UserAddedNadir_Index;
    
    public ArrayList<Double> peakValue_Max;
    public ArrayList<Double> peakValue_Min;
    
    private double[] MaxPeakTime;   
    private double[] MaxPeaks;
    
    private double[] Peak_Time; 
    
    private double[] MinPeaks;
    private double[] MinPeakTime;
    
    
    private double[] Amplitude;
    private double[] SpikeWidth;
    private double[] SpikeWidthThr_Y;
    private double[] SpikeTimeStart;
    private double[] SpikeTimeEnd;
    private double[] SpikeArea;
    private int[] IndexFirstSampleAfterTimeStart;
    private int[] IndexFristSampleBeforeTimeEnd;
    
    private double[] SpikeTriangle;
    
    private double[] Perpendicular_Line_to_SW;
    
    private double[] ReleasingRate;
    private double[] RemovingRate;

    public boolean IsPeakFound;
    private double PeakPercent;
    
    public boolean TimeIsAddedToSection;
    
    public PlotPoints isiTimePlot;
    
    public boolean IsFiltered;
     
    private double[] ISI;
    
    private double Tav = 0;
    
    private double STD = 0;
    
    public int getSecID()
    {
        return section_ID;
    }
    
    public double getTav()
    {
        this.computeTav();
        return this.Tav;
    }
    
    public double getSTD()
    {
        this.computeSigmaISIs();
        
        return this.STD;
    }
    public double getFreqSNR()
    {
        return getTav()/getSTD();
    }
    
    //time from nadir to peak
    public double getTimeToPeak(int PeakNo)
    {
        if(this.IsPeakFound && PeakNo>=0 && PeakNo<this.getNumberOfMaxPeaks())
        {
            return this.section_time.get(this.peakIndex_Max.get(PeakNo)) -
                    this.section_time.get(this.peakIndex_Min.get(PeakNo));
        }
        else
            return 0;
    }
    
    public double getTimeToPeakAverage(){
        double ttp = 0;
       if(this.IsPeakFound && this.peakIndex_Max!=null && this.peakIndex_Min!=null) 
       {
           for(int i=0; i< this.peakIndex_Max.size(); i++){
           ttp = ttp +  this.section_time.get(this.peakIndex_Max.get(i)) -
                    this.section_time.get(this.peakIndex_Min.get(i));
            }
           return ttp/this.peakIndex_Max.size();
       }
       else   
        return 0;
    }
    
    private void ComputeCalciumReleasingRate()
    {
        this.ReleasingRate = null;
        if(this.IsPeakFound && SpikeTimeStart!= null && SpikeTimeEnd!=null && SpikeWidthThr_Y!=null)
        {
           this.ReleasingRate = new double[this.getPeakNumber()];
          
           for(int i=0; i< this.getPeakNumber(); i++)
           {
              double x1= this.SpikeTimeStart[i];
              double y1 = this.SpikeWidthThr_Y[i];
              
              double x2 = this.getMaxPeakTime(i);
              double y2 = this.getMaxPeakValue(i);
              
              this.ReleasingRate[i] = (y2-y1)/(x2-x1);             
           }
        }
    }
    
    private void computeCalciumRemovingRate(){
        this.RemovingRate = null;
        if(this.IsPeakFound && SpikeTimeStart!= null && SpikeTimeEnd!=null && SpikeWidthThr_Y!=null)
        {
           this.RemovingRate = new double[this.getPeakNumber()];
           for(int i=0; i< this.getPeakNumber(); i++)
           {             
              double x2 = this.getMaxPeakTime(i);
              double y2 = this.getMaxPeakValue(i);
              
              double x3= this.SpikeTimeEnd[i];
              double y3 = this.SpikeWidthThr_Y[i];
              
              this.RemovingRate[i] = (y2-y3)/(x3-x2);
              
           }
        }
    }
    
    public double getReleasingRate(int PeakNo)
    {
       this.ComputeCalciumReleasingRate();
       if(this.ReleasingRate!=null && this.ReleasingRate.length>0
               && PeakNo>=0 && PeakNo<this.ReleasingRate.length){
           return this.ReleasingRate[PeakNo];
       }
       else
           return 0;
    }
    
    public double getRemovingRate(int PeakNo)
    {
       this.computeCalciumRemovingRate();
       if(this.RemovingRate!=null && this.RemovingRate.length>0
               && PeakNo>=0 && PeakNo<this.RemovingRate.length){
           return this.RemovingRate[PeakNo];
       }
       else
           return 0;
    }
    
    public double getAverageReleasingRate(){
        this.ComputeCalciumReleasingRate();
       if(this.ReleasingRate!=null && this.ReleasingRate.length>0)
       {
           return Utils.getAverage(this.ReleasingRate);
       }
       else
           return 0;
    }
    
    public double getAverageRemovingRate(){
        this.computeCalciumRemovingRate();
       if(this.RemovingRate!=null && this.RemovingRate.length>0)
       {
           return Utils.getAverage(this.RemovingRate);
       }
       else
           return 0;
    }
    //Effective spike area
    public double getSpikeAreaAverage(){
        if(this.SpikeArea !=null)
        {
           return Utils.getAverage(this.SpikeArea);
        }
        else
            return 0;
    }
    
    public double getSpikeAreaSD(){
        if(this.SpikeArea !=null)
        {
            return Utils.getSD(this.SpikeArea);
        }
        else
            return 0;       
    }
    
    public double getSpikeTriangle()
    {
        if(this.SpikeTriangle!=null)
        {
            return Utils.getAverage(this.SpikeTriangle);
        }
        else
            return 0;
    }
    
    public double getEffectiveSpikeArea(int PeakNo)
    {
        if(this.SpikeArea!=null && PeakNo>=0 && PeakNo<this.SpikeArea.length)
            return this.SpikeArea[PeakNo];
        else
            return 0;
    }
    
    public int getNumberOfMaxPeaks()
    {
        return this.peakIndex_Max.size();
    }
    
    public double getAmplitude(int PeakNo)
    {
        if(this.Amplitude!=null && PeakNo>=0 && PeakNo<this.Amplitude.length)
            return this.Amplitude[PeakNo];
        else
            return 0;
    }
    
    public double getSpikeWidth(int PeakNo)
    {
        if(this.SpikeWidth != null && PeakNo >= 0 && PeakNo < this.SpikeWidth.length)
            return this.SpikeWidth[PeakNo];
        else
            return 0;
    }
    
    public double getSpikeWidthThrY(int PeakNo){
        if(this.SpikeWidthThr_Y !=null && this.SpikeWidthThr_Y.length > 0)
        {
            return this.SpikeWidthThr_Y[PeakNo];
        }
        else
            return -1;  
    }
    
    public double getISI(int PeakNo)
    {
        if(this.ISI!=null && PeakNo>=0 && PeakNo<this.ISI.length)
            return this.ISI[PeakNo];
        else
            return 0;
    }
    
    public double getSpikeStartTime(int PeakNo){
        
        if(this.SpikeTimeStart!=null && this.SpikeTimeStart.length>0)
        {
            return this.SpikeTimeStart[PeakNo];
        }
        else
            return -1;        
    }
    
    public double getSpikeEndTime(int PeakNo){
        if(this.SpikeTimeEnd!=null && this.SpikeTimeEnd.length>0)
        {
            return this.SpikeTimeEnd[PeakNo];
        }
        else
            return -1;  
    }
   
    //This function return the summation of the half length of 2 consequence spikes
//    public double getSpikeWidthofTwoPeaks(int PeakNo)
//    {
//        if(this.StartEndTimeOfSpikeWiths!=null &&  this.ISI!=null && PeakNo>=0 && PeakNo<this.ISI.length)
//        {
//            double startTime_SecondPeak = this.StartEndTimeOfSpikeWiths[(PeakNo+1)*2];
//            double MaxPeakTime_SecondPeak = this.getMaxPeakTime(PeakNo+1);
//            
//            double endTimeFirstPeak = this.StartEndTimeOfSpikeWiths[PeakNo*2+1];
//            double MaxPeakTime_FirstPeak = this.getMaxPeakTime(PeakNo);
//            //System.out.println(String.valueOf((MaxPeakTime_SecondPeak - startTime_SecondPeak) + (endTimeFirstPeak - MaxPeakTime_FirstPeak)));
//            return (MaxPeakTime_SecondPeak - startTime_SecondPeak) + (endTimeFirstPeak - MaxPeakTime_FirstPeak);
//        }
//        else
//            return 0;
//    }
    
    public double getPeakTimeForOnePeak(int PeakNo)
    {
        if(this.Peak_Time!= null && PeakNo>=0 && PeakNo<this.Peak_Time.length )
            return this.Peak_Time[PeakNo];
        else
            return 0;
    }
    
    public double[] getISIs()
    {
        return this.ISI;
    }
    
    public double[] computeAndGetISIs()
    {
        this.computeISI();
        return this.ISI;
    }
    
    public void setPeakPercent(double PeakPercent)
    {
        this.PeakPercent = PeakPercent;
    }
    
    public boolean IsISILengthBiggerOne()
    {
        if(this.ISI!=null)
        {
            return this.ISI.length>1;
        }
        else 
            return false;
    }
    
    private int relatedSignalID = -1;
    
    public int getSignalID()
    {
        return this.relatedSignalID;
    }
    
    public double getMaxPeakTime(int i)
    {
        if(i<this.peakIndex_Max.size())
        {
            return this.section_time.get(this.peakIndex_Max.get(i));
        }
        else
            return -1;
    }
    
    public double getMaxPeakValue(int i)
    {
        if(i<this.peakIndex_Max.size())
            return (this.peakValue_Max.get(i));
        else
            return -1;
        
    }
    
    public double getMinPeakTime(int i)
    {
        if(i<this.peakIndex_Min.size())
        {
            return this.section_time.get(this.peakIndex_Min.get(i));
        }
        else
            return -1;
    }
    
    public double getMinPeakValue(int i)
    {
        if(i<this.peakIndex_Min.size())
            return this.peakValue_Min.get(i);
        else
            return -1;
    }
    
    public String getSectionName(){
        return this.section_Name;
    }
    

    public double getIntensitiesMean(){
        return Utils.getAverage(this.section_value);
    }
    
    public double getIntensitiesSD()
    {
        return Utils.getSD(this.section_value);
    }
    
    public double getSNR()
    {
        return getIntensitiesMean()/getIntensitiesSD();
    }
    
    public double getEnergy(){
        
        double Square_sum = 0;
        
        for(int i=0; i<this.section_time.size(); i++)
        {
            Square_sum = Square_sum + Math.pow(this.section_value.get(i), 2);
        }
        
        return Square_sum;        
    }
    
    public double getPower(){
        
        double Square_sum = 0;
        
        for(int i=0; i<this.section_time.size(); i++)
        {
            Square_sum = Square_sum + Math.pow(this.section_value.get(i), 2);
        }
        
        return Square_sum/this.section_time.size();        
    }
    //compute rms of whole section as the current of whole signal
    //RMS is an estimation of signal current or signal amplitude
    //RMS shows the DC value of AC signal
    public double getRootMeanSquareSection(){
        
        double Square_sum = 0;
        
        for(int i=0; i<this.section_time.size(); i++)
        {
            Square_sum = Square_sum + Math.pow(this.section_value.get(i), 2);
        }
        
        return Math.sqrt(Square_sum/this.section_time.size());        
    }
    //section charge Q= rms of section * Average spike width
    public double getRMSCharge()
    {
        if(this.IsPeakFound && this.ISI!= null){          
            return getRootMeanSquareSection()*this.getMeanSW();            
        }
        else
            return 0;
    }
    
    public double getEffectiveSectionCharge()
    {
        if(this.IsPeakFound)
        {
            double SumSpikeArea = 0;
            if(this.SpikeArea!=null && this.SpikeArea.length>0)
            {
                for(int i=0; i<this.getPeakNumber();i++)
                {
                    SumSpikeArea +=this.SpikeArea[i];

                }
                return SumSpikeArea;
            }
            else
                return 0;
        }
        else 
            return 0;
    }
    
    //ISI charge = rms of one ISI * ISI Time
//    public double getRMSChargeForISI(int PeakNo){
//        
//        if(this.IsPeakFound && this.ISI!= null && PeakNo>0 && PeakNo<this.getPeakNumber())
//        {
//           int index_start = this.peakIndex_Max.get(PeakNo);
//           int index_end = this.peakIndex_Max.get(PeakNo+1);
//           double square_Sum =0;
//           
//           for(int i=index_start; i<=index_end; i++){
//               square_Sum = square_Sum + Math.pow(this.section_value.get(i), 2);
//           }
//           
//           return  Math.sqrt(square_Sum/(index_end-index_start+1))*this.ISI[PeakNo];                            
//        }
//        else
//            return 0;
//    }
    
//    public double getISICalciumIonsCount(int PeakNo){
//        if(this.IsPeakFound && PeakNo>0 && PeakNo<this.getPeakNumber()){            
//            return this.getRMSChargeForISI(PeakNo)/CALCIUM_CHARGE;       
//        }
//        else
//            return 0;
//    }
    
    public double getSectionCalciumIonsCount(){
      if(this.IsPeakFound){            
            return this.getRMSCharge()/CALCIUM_CHARGE;       
        }
        else
            return 0;  
    }
    
//    public double getISICalciumMoles(int PeakNo){
//        if(this.IsPeakFound && PeakNo>0 && PeakNo<this.getPeakNumber()){              
//            return this.getISICalciumIonsCount(PeakNo)/AVOGADRO_CONSTANT;               
//        }
//        else
//            return 0;
//    }
    
    public double getSectionCalciumMoles(){
       if(this.IsPeakFound){
           return this.getSectionCalciumIonsCount()/AVOGADRO_CONSTANT;         
       } 
       else
           return 0;        
    }
    
    public double getMeanPeakValue(){
        if(this.IsPeakFound && this.peakValue_Max!=null)
        {
            return Utils.getAverage(this.peakValue_Max);
        }
        else
            return 0;        
    }
    
    public double getMeanNadirValue(){
        if(this.IsPeakFound && this.peakValue_Min!=null)
        {
            return Utils.getAverage(this.peakValue_Min);
        }
        else
            return 0;        
    }
    
   
    public Sections(String Name, int ID,int SignalID)
    {
        this.section_Name = Name;
        this.section_ID = ID;
        this.relatedSignalID = SignalID;
        this.startIndex_InSignal = -1;
        this.EndIndex_InSignal = -1;        
        this.PeakPercent = 30; 
        this.section_time = new ArrayList<Double>();
        this.section_value = new ArrayList<Double>();
        this.TimeIsAddedToSection = false;
        this.IsPeakFound = false;
        this.IsFiltered = false;       
    }
    
    public double[] getMaxPeakValues()
    {
        int numberOfMaxPeaks = this.peakIndex_Max.size();
        MaxPeaks = null;
        MaxPeaks = new double[numberOfMaxPeaks]; 
        for(int i=0; i<numberOfMaxPeaks; i++ )
        {
            MaxPeaks[i] = this.peakValue_Max.get(i);
        }
        return MaxPeaks;
    }
     
    public double[] getMaxPeakTime()
    {
        int numberOfMaxPeaks = this.peakIndex_Max.size();
        this.MaxPeakTime = null;
        this.MaxPeakTime = new double[numberOfMaxPeaks]; 
        for(int i=0; i<numberOfMaxPeaks; i++)
        {
            this.MaxPeakTime[i] = this.section_time.get(this.peakIndex_Max.get(i));
        }
        return this.MaxPeakTime;
    }
    
    public void computePeakTime()
    {
        this.Peak_Time = null;
        this.Peak_Time = new double[this.getPeakNumber()]; 
        for(int i=0; i<this.getPeakNumber(); i++)
        {
            this.Peak_Time[i] = this.section_time.get(this.peakIndex_Max.get(i));
        }
    }
    
    public double[] getPeakTime()
    {
        return this.Peak_Time;
    } 
    
    public double getPeakTime(int peakNo)
    {
        if(this.IsPeakFound && peakNo>=0 && peakNo<this.getPeakNumber())
        {
            return this.section_time.get(this.peakIndex_Max.get(peakNo));
        }
        else
            return -1;
    }  
    
    public double[] getMinPeakValues()
    {
        int numberOfMinPeaks = this.peakIndex_Min.size();
        this.MinPeaks = null;
        MinPeaks= new double[numberOfMinPeaks]; 
        for(int i=0; i<numberOfMinPeaks; i++ )
        {
            MinPeaks[i] = this.peakValue_Min.get(i);
        }
        return MinPeaks;
    }
    
    public double[] getMinPeakTime()
    {
        int numberOfMinPeaks = this.peakIndex_Min.size();
        this.MinPeakTime = null;
        MinPeakTime = new double[numberOfMinPeaks]; 
        for(int i=0; i<numberOfMinPeaks; i++ )
        {
            MinPeakTime[i] = this.section_time.get(this.peakIndex_Min.get(i));
        }
        return MinPeakTime;
    }
    
    public double getMeanSW()
    {
        if(this.SpikeWidth!=null)
        {
            return Utils.getAverage(this.SpikeWidth);
        }
        else
            return 0;
    }
    
    public double getMeanAMP()
    {
        if(this.Amplitude!=null)
        {
            return Utils.getAverage(this.Amplitude);
        }
        else
            return 0;
    }
    
    public double getSpikeWidthSD()
    {
        if(this.SpikeWidth!=null) {
            return Utils.getSD(this.SpikeWidth);
        } else return -1;
    }
    
    public double getAmplitudeSD()
    {
        if(this.Amplitude!=null)
        {
            return Utils.getSD(this.Amplitude);
        }
        else
            return -1;
    }
    
    
    public boolean ComputeSpikeWidthAndAMP2()
    {        
        boolean SW_IsFound = false;
        this.Amplitude = null;
        this.SpikeWidth = null;
        
        this.Perpendicular_Line_to_SW = null;
        this.SpikeWidthThr_Y = null;
        this.SpikeTimeStart  = null;
        this.SpikeTimeEnd = null;
        this.IndexFirstSampleAfterTimeStart = null;
        this.IndexFristSampleBeforeTimeEnd = null;
        this.SpikeArea = null;
        this.SpikeTriangle = null;
                
        
        if(this.getPeakNumber()>0)
        {
            double[] amplitude_tmp = new double[this.peakIndex_Max.size()];
            double[] spikeWidth_tmp = new double[this.peakIndex_Max.size()]; 
            
            double[] Perpendicular_Line_to_SW_tmp = new double[this.peakIndex_Max.size()];
            double[] SpikeTimeStart_tmp = new double[this.peakIndex_Max.size()];
            double[] SpikeTimeEnd_tmp = new double[this.peakIndex_Max.size()];
            
            int[] IndexFirstSampleAfterTimeStart_tmp = new int[this.peakIndex_Max.size()];
            int[] IndexFristSampleBeforeTimeEnd_tmp = new int[this.peakIndex_Max.size()];
            
            double[] SpikeWidthThr_Y_tmp = new double[this.peakIndex_Max.size()];

            ArrayList<Integer> peakIndex_Max_Temp = new ArrayList();
            ArrayList<Double> peakValue_Max_Temp = new ArrayList();
            peakIndex_Max_Temp.addAll(this.peakIndex_Max);
            peakValue_Max_Temp.addAll(this.peakValue_Max);


            if (this.peakIndex_Max.get(0) < this.peakIndex_Min.get(0)) // if signal starts with a max 
            {// remove it, because signal should be start with a min
                peakIndex_Max_Temp.remove(0);
                peakValue_Max_Temp.remove(0);
            }
            int lastElement_Index = peakIndex_Max.size() - 1;
            if (this.peakIndex_Max.get(lastElement_Index) > this.peakIndex_Min.get(peakIndex_Min.size() - 1)) {
                // signal should be ended with a minimum
                peakIndex_Max_Temp.remove(lastElement_Index);
                peakValue_Max_Temp.remove(lastElement_Index);
            }
            // we should have 2 minimum points at least
            if(peakIndex_Max_Temp.size()+ 1 == this.peakIndex_Min.size())
            {
                int count = 0;
                for(int i=0; i< this.peakIndex_Max.size(); i++)
                {
                    //compute amplitude of peak that is: 
                    double x1 =this.section_time.get(this.peakIndex_Min.get(i));
                    double y1 = this.peakValue_Min.get(i);

                    double x2 =this.section_time.get(this.peakIndex_Max.get(i));
                    double y2 = this.peakValue_Max.get(i);
                    double min_y_diff_from_nadir = this.peakValue_Max.get(i) - this.peakValue_Min.get(i);
                    
                    boolean nextMin_IsFound = false;
                    
                    for(int k = this.peakIndex_Max.get(i)+1 ; k<this.peakIndex_Min.get(i+1); k++)
                    {
                        //find the lowest magnitude difference between nadir and the point that comes in the
                        //window between peak and next nadir while this point
                        //should not be local local maximum otherwise it is not accepeted
                        //this condition leads to ignore noises since local maximum between two peaks are considered as noise
                        
                        if(this.section_value.get(k) < this.section_value.get(k+1) &&
                                (this.section_value.get(k)- this.peakValue_Min.get(i)) < min_y_diff_from_nadir)
                        {
                            x2 = this.section_time.get(k);
                            y2 = this.section_value.get(k);
                            nextMin_IsFound = true;
                        }
                    }
                    if(!nextMin_IsFound)
                    {
                        x2 = this.section_time.get(this.peakIndex_Min.get(i+1));
                        y2 = this.section_value.get(this.peakIndex_Min.get(i+1));
                        nextMin_IsFound = true;
                    }

                    double slope = (y2-y1)/(x2-x1);
                    double intercept = y2 - x2*slope;

                    double crossPointY = slope* this.section_time.get(this.peakIndex_Max.get(i)) + intercept;
                    
                    amplitude_tmp[i] = this.peakValue_Max.get(i) - crossPointY;
                    
                   
                    if(amplitude_tmp[i] > 0)
                    {
                        count++;
                        double baseLine = crossPointY;
                        
                        double SW_Threshold = amplitude_tmp[i]*0.2 + baseLine;
                        
                        double TimeStart =0; // the time which spike width start
                        int TimeStart_Index = 0;
                        double TimeEnd = 0;
                        int TimeEnd_Index = 0;
                        
                        for(int j = this.peakIndex_Min.get(i); j<= peakIndex_Max_Temp.get(i); j++)
                        {
                            if(this.section_value.get(j) > SW_Threshold) //
                            {
                                double a = (this.section_value.get(j) - this.section_value.get(j-1))/
                                        (this.section_time.get(j) - this.section_time.get(j-1)); //slope
                                if(a!=0)
                                {
                                    double b = this.section_value.get(j) - a*this.section_time.get(j); //intercept
                                    TimeStart = (SW_Threshold - b)/a;
                                    TimeStart_Index = j;
                                    break;
                                }
                            }
                        }
                        if(TimeStart ==0)
                        {
                            TimeStart = this.section_time.get(this.peakIndex_Min.get(i));
                            TimeStart_Index = this.peakIndex_Min.get(i);
                        }

                        for(int j = peakIndex_Max_Temp.get(i);j<= this.peakIndex_Min.get(i+1); j++ )
                        {
                            if(this.section_value.get(j) < SW_Threshold)
                            {
                                double a = (this.section_value.get(j) - this.section_value.get(j-1))/(this.section_time.get(j) - this.section_time.get(j-1));
                                if(a!=0)
                                {
                                    double b = this.section_value.get(j) - a*this.section_time.get(j);
                                    TimeEnd = (SW_Threshold - b)/a;
                                    TimeEnd_Index = j-1;
                                    break;
                                }
                            }
                        }
                        if(TimeEnd == 0)
                        {
                            TimeEnd = this.section_time.get(this.peakIndex_Min.get(i+1));
                            TimeEnd_Index = this.peakIndex_Min.get(i+1);
                        }

                        spikeWidth_tmp[i] = TimeEnd - TimeStart;
                        SpikeWidthThr_Y_tmp[i] = SW_Threshold;
                        SpikeTimeStart_tmp[i] = TimeStart;
                        SpikeTimeEnd_tmp[i] = TimeEnd;
                        //compute area under the peak and spikewidthline
                        //draw spike width line that have zero slope and intercept= SW_Threshold
                        //the perpendicular line from peak that cross spike line, has the length:
                        Perpendicular_Line_to_SW_tmp[i] = this.peakValue_Max.get(i) - SW_Threshold;
                        IndexFirstSampleAfterTimeStart_tmp[i] = TimeStart_Index;
                        IndexFristSampleBeforeTimeEnd_tmp[i] = TimeEnd_Index;
                        
                        SW_IsFound = true;
                    }
                    else
                    {
                       amplitude_tmp[i] =0; 
                       spikeWidth_tmp[i] = 0;
                       SpikeWidthThr_Y_tmp[i] = 0; 
                       Perpendicular_Line_to_SW_tmp[i]= 0;
                       SpikeTimeStart_tmp[i] = 0;
                       SpikeTimeEnd_tmp[i] = 0;
                       IndexFirstSampleAfterTimeStart_tmp[i] = -1;
                       IndexFristSampleBeforeTimeEnd_tmp[i] =-1;
                    }
                }

                this.Amplitude = new double[this.peakIndex_Max.size()];
                this.SpikeWidth = new double[this.peakIndex_Max.size()];
               
                this.Perpendicular_Line_to_SW = new double[this.peakIndex_Max.size()];
                this.SpikeTimeStart = new double[this.peakIndex_Max.size()];
                this.SpikeTimeEnd = new double[this.peakIndex_Max.size()];
                this.SpikeWidthThr_Y = new double[this.peakIndex_Max.size()];
                this.IndexFirstSampleAfterTimeStart = new int[this.peakIndex_Max.size()];
                this.IndexFristSampleBeforeTimeEnd = new int[this.peakIndex_Max.size()];
                this.SpikeArea = new double[this.peakIndex_Max.size()];
                this.SpikeTriangle = new double[this.peakIndex_Max.size()];
                
                for(int i=0; i< this.peakIndex_Max.size() ; i++)
                {                    
                    this.Amplitude[i] = amplitude_tmp[i];
                    this.SpikeWidth[i] = spikeWidth_tmp[i];
                    this.Perpendicular_Line_to_SW[i] = Perpendicular_Line_to_SW_tmp[i];
                 
                    this.SpikeTimeStart[i] = SpikeTimeStart_tmp[i];
                    this.SpikeTimeEnd[i] = SpikeTimeEnd_tmp[i];
                    this.SpikeWidthThr_Y[i] = SpikeWidthThr_Y_tmp[i];
                    this.IndexFirstSampleAfterTimeStart[i] = IndexFirstSampleAfterTimeStart_tmp[i];
                    this.IndexFristSampleBeforeTimeEnd[i] = IndexFristSampleBeforeTimeEnd_tmp[i];
                    this.SpikeTriangle[i] = (Perpendicular_Line_to_SW_tmp[i]*spikeWidth_tmp[i])/2;

                    this.computeEffectiveSpikeArea();
                }
            }
            else
            {
                SW_IsFound = false; 
                this.SpikeWidth = null;
            }
            peakIndex_Max_Temp = null;
            peakValue_Max_Temp = null;
            amplitude_tmp = null;
            spikeWidth_tmp = null;
            SpikeWidthThr_Y_tmp = null; 
            Perpendicular_Line_to_SW_tmp= null;
            SpikeTimeStart_tmp = null;
            SpikeTimeEnd_tmp = null;
            IndexFristSampleBeforeTimeEnd_tmp = null;
            IndexFirstSampleAfterTimeStart_tmp = null;
                       
        }
        return SW_IsFound;        
    }
    
    public void computeEffectiveSpikeArea()
    {            
        if(this.IndexFirstSampleAfterTimeStart!=null && this.IndexFristSampleBeforeTimeEnd!=null
                && this.IndexFirstSampleAfterTimeStart.length==this.getPeakNumber() 
                && this.IndexFristSampleBeforeTimeEnd.length ==this.getPeakNumber())
        {
            for(int i=0; i<this.getPeakNumber();i++)
            {
               if(IndexFirstSampleAfterTimeStart[i]!=-1 && IndexFristSampleBeforeTimeEnd[i]!=-1 && this.SpikeWidthThr_Y[i]!=0)
               {
                this.SpikeArea[i] = Math.abs((this.section_time.get(IndexFirstSampleAfterTimeStart[i]) - this.SpikeTimeStart[i])*
                                     (this.section_value.get(IndexFirstSampleAfterTimeStart[i]) - this.SpikeWidthThr_Y[i]));  
                //for each spike compute the surface under the spike
               for(int j= IndexFirstSampleAfterTimeStart[i]; j<IndexFristSampleBeforeTimeEnd[i]; j++)
               {
                   this.SpikeArea[i] += Math.abs((this.section_time.get(j+1)-this.section_time.get(j))*
                                        (this.section_value.get(j+1)-this.section_value.get(j)));
               }
               this.SpikeArea[i] +=  Math.abs((this.SpikeTimeEnd[i] - this.section_time.get(IndexFristSampleBeforeTimeEnd[i]))*
                                     (this.SpikeWidthThr_Y[i] - this.section_value.get(IndexFristSampleBeforeTimeEnd[i]))); 

               this.SpikeArea[i] = this.SpikeArea[i]/2;
               }
            }
        }
        
    }

    public boolean EditSectionValues(Signal sig)
    {
        boolean SectionValueIsEditted = false;
        if(this.startIndex_InSignal!= -1 && this.EndIndex_InSignal!=-1 && this.startIndex_InSignal<this.EndIndex_InSignal)
        {           
            if(this.section_time.size() == (EndIndex_InSignal - startIndex_InSignal+1))
            {
                for(int i = 0; i<this.section_time.size(); i++)
                {
                    this.section_value.set(i, sig.sig_value.get(i+startIndex_InSignal));
                    SectionValueIsEditted = true;
                }
            }
        }
        if(SectionValueIsEditted)
        {
            if(sig.IsNormalized_temp || sig.IsBackgroundRemoved_temp)
            {
                 for(int i=0; i<this.peakIndex_Max.size(); i++) //edit peakvalue_max and peakvalue_min
                {
                   this.peakValue_Max.set(i, this.section_value.get(this.peakIndex_Max.get(i)));
                }
                for(int i=0; i<this.peakIndex_Min.size(); i++)
                {
                   this.peakValue_Min.set(i, this.section_value.get(this.peakIndex_Min.get(i)));
                }
                this.ComputeSpikeWidthAndAMP2();
                
            }
            else 
            {               
                this.IsPeakFound = false;
                this.peakIndex_Max = null;
                this.peakIndex_Min = null;
                this.peakValue_Max = null;
                this.peakValue_Min = null;
                this.Amplitude = null;
                this.SpikeWidth = null;
                this.UserAddedNadir_Index = null;
                this.UserAddedPeak_Index = null;
            }
           
        }
        return SectionValueIsEditted;
    }
    
    public void AddTimeAndValue(ArrayList<Double> time, ArrayList<Double> value)
    {
        this.section_time.clear();
        this.section_value.clear();
        for(int i=0; i<time.size(); i++)
        {
            this.section_time.add(time.get(i));
            this.section_value.add(value.get(i));
        } 
        time = null;
        value = null;
        this.TimeIsAddedToSection = true;
        this.IsPeakFound = false;
        this.peakIndex_Max = null;
        this.peakIndex_Min = null;
        this.peakValue_Max = null;
        this.peakValue_Min = null;
        this.Amplitude = null;
        this.SpikeWidth = null;
        this.UserAddedNadir_Index = null;
        this.UserAddedPeak_Index = null;
      
    }
    
    public void Normalize(double MinSignal, double MaxAmp)
    {
        for (int i = 0; i < this.section_time.size(); i++) {
            double normalValue = (this.section_value.get(i) - MinSignal) / MaxAmp;
            this.section_value.set(i, normalValue);
            
        }        
    }    
    public void addNadir(int IndexInSection){
       if(IsPeakFound)
       { 
           //find the new minpeakIndex in maxPeakIndex to know where it should be replaced
           int Index_InMinPeakIndex = -1;
           for(int i=0; i<this.peakIndex_Max.size();i++){
               if(this.peakIndex_Max.get(i) > IndexInSection){
                   Index_InMinPeakIndex = i;
                   break;
               }
            }
           if(Index_InMinPeakIndex != -1){
             this.peakIndex_Min.set(Index_InMinPeakIndex,IndexInSection);
             this.peakValue_Min.set(Index_InMinPeakIndex, this.section_value.get(IndexInSection));
           }
           else
           {
               //if user wants to change the place of last minimum peak in the section by himself
              if(IndexInSection > this.peakIndex_Max.get(this.getPeakNumber()-1))
              {
                  this.peakIndex_Min.set(this.peakIndex_Min.size()-1,IndexInSection); // the last element
                  this.peakValue_Min.set(this.peakIndex_Min.size()-1, this.section_value.get(IndexInSection));
              }
           }
           if(this.UserAddedNadir_Index==null){
               this.UserAddedNadir_Index = new ArrayList();
           }
           this.UserAddedNadir_Index.add(IndexInSection);
           
           this.ComputeSpikeWidthAndAMP2();
           this.computeISI();
       }         
    }    
    public void addPeak(int IndexInSection)
    {
        if(this.UserAddedPeak_Index == null)
        {
           this.UserAddedPeak_Index = new ArrayList();
        }
        if(IsPeakFound)
        {                       
            this.peakIndex_Max.add(IndexInSection);
            this.UserAddedPeak_Index.add(IndexInSection);
            Collections.sort(this.peakIndex_Max);
            this.peakValue_Max.clear();
            for(int i =0; i< peakIndex_Max.size(); i++)
            {
                peakValue_Max.add(this.section_value.get(peakIndex_Max.get(i)));
            }              
        }
        else
        {
            if(this.peakIndex_Max == null){
                this.peakIndex_Max = new ArrayList();
                this.peakIndex_Min = new ArrayList();
                this.peakValue_Max = new ArrayList();
                this.peakValue_Min = new ArrayList();
            }
            if(this.peakIndex_Max.isEmpty()) // by peak threshold no peak is founded
            {
                this.peakIndex_Max.add(IndexInSection);
                this.peakValue_Max.add(this.section_value.get(IndexInSection));
                this.UserAddedPeak_Index.add(IndexInSection);
                this.IsPeakFound = true;
            }
            
        }       
        AddNadirAutomatically();             
        this.refineMinPeaks();        
        this.ComputeSpikeWidthAndAMP2();
        this.computeISI();
        
    }
    
// Add minimum peak of new maximum peak
    //each section should start with a nadir and end with a nadir. So the number of elements
    // of peakIndex_Min array is = peakIndex_Max + 1
    private boolean AddNadirAutomatically(){        
        
        boolean MinPeakIsFound = false;
        int minimumIndex = -1;
        double minimumValue = -1;
        int PeakIndexMax_Size = peakIndex_Max.size();
        if(this.peakIndex_Min.isEmpty())
        {
           minimumIndex = this.peakIndex_Max.get(0);
           minimumValue = this.peakValue_Max.get(0); 
           for(int j = this.peakIndex_Max.get(0); j>0 ;j--)
           {
                if(minimumValue > this.section_value.get(j))
                {
                    minimumValue = this.section_value.get(j);
                    minimumIndex = j;
                }
                else
                    if(this.peakIndex_Max.get(0) != minimumIndex) // minimum value cannot be equal to maximum value
                    {
                        break;
                    }                    
            }
            this.peakIndex_Min.add(minimumIndex);
            this.peakValue_Min.add(minimumValue);
            //Add minimum peak to the end of signal
            minimumIndex = this.peakIndex_Max.get(0);
            minimumValue = this.peakValue_Max.get(0); 
            for(int j = this.peakIndex_Max.get(0); j < this.section_value.size(); j++)
            {
                if(minimumValue > this.section_value.get(j))
                {
                    minimumValue = this.section_value.get(j);
                    minimumIndex = j;
                }
                else
                    if(this.peakIndex_Max.get(0) != minimumIndex)
                    {
                        break;
                    }
            }
            this.peakIndex_Min.add(minimumIndex);
            this.peakValue_Min.add(minimumValue);
            MinPeakIsFound = true;
        }
        else
        {
            for(int i = 0; i< PeakIndexMax_Size; i++)
            {
                if(this.peakIndex_Max.get(i)< this.peakIndex_Min.get(i))
                {
                    minimumIndex = this.peakIndex_Max.get(i);
                    minimumValue = this.peakValue_Max.get(i);
                    if(i == 0)// peak is added to the begining of signal
                    {
                        for(int j = this.peakIndex_Max.get(0); j>0 ;j--)
                        {
                            if(minimumValue >= this.section_value.get(j))
                            {
                                minimumValue = this.section_value.get(j);
                                minimumIndex = j;
                            }
                            else
                                break;
                        }
                        MinPeakIsFound = true;
                    }
                    else
                    {
                        for(int j=this.peakIndex_Max.get(i); j> this.peakIndex_Max.get(i-1); j-- )
                        {
                            if(minimumValue > this.section_value.get(j))//finding the first local minimum
                            {
                                minimumValue = this.section_value.get(j);
                                minimumIndex = j;
                            }
                            if(minimumValue < this.section_value.get(j-1) && minimumIndex!= this.peakIndex_Max.get(i))
                                // minimum should not be equal to maximum
                                    break;                        
                        }
                        MinPeakIsFound = true;
                    }
                    break;
                }
            }
            if(!MinPeakIsFound && PeakIndexMax_Size == this.peakIndex_Min.size()) // peak is added to the end of section or signal
            {
                this.peakIndex_Min.remove(PeakIndexMax_Size-1);//remove the last nadir at the end of section
                this.peakValue_Min.remove(PeakIndexMax_Size-1);
                
                minimumIndex = this.peakIndex_Max.get(PeakIndexMax_Size-1);//et the last peak
                minimumValue = this.peakValue_Max.get(PeakIndexMax_Size-1);

                for(int j=this.peakIndex_Max.get(PeakIndexMax_Size-1) ; j >this.peakIndex_Max.get(PeakIndexMax_Size-2); j-- )
                {
                    if(minimumValue > this.section_value.get(j))
                    {
                        minimumValue = this.section_value.get(j);
                        minimumIndex = j;
                    }
                    if(minimumValue < this.section_value.get(j-1) && minimumIndex!= this.peakIndex_Max.get(PeakIndexMax_Size-1))
                        // minimum should not be equal to maximum
                        break;                        
                }
                // Add the last nadir to the end of section
                int lastminimumIndex = this.peakIndex_Max.get(PeakIndexMax_Size-1);
                double lastminimumValue = this.peakValue_Max.get(PeakIndexMax_Size-1);
                for(int j= this.peakIndex_Max.get(PeakIndexMax_Size-1) ; j< this.section_value.size(); j++)
                {
                    if(minimumValue > this.section_value.get(j))
                    {
                        lastminimumValue = this.section_value.get(j);
                        lastminimumIndex = j;
                    }
                    else
                        if(lastminimumIndex != this.peakIndex_Max.get(PeakIndexMax_Size-1))
                        {
                            break;
                        }
                }
                this.peakIndex_Min.add(lastminimumIndex);
                this.peakValue_Min.add(lastminimumValue);                
                MinPeakIsFound = true;
                
            }
            if(MinPeakIsFound)
            {
                this.peakIndex_Min.add(minimumIndex);
                Collections.sort(this.peakIndex_Min);
                this.peakValue_Min.clear();
                for(int i =0; i< peakIndex_Min.size(); i++)
                {
                    peakValue_Min.add(this.section_value.get(peakIndex_Min.get(i)));
                }  
            }
        }
        
        return MinPeakIsFound;
    }
    
    public void removePeakandNadir(int IndexIn_PeakIndexMax_PeakIndexMin,boolean IsIndexIn_Peaks,boolean IsIndexIn_Nadirs)
    {
        if(IsPeakFound)
        {   
            //user cannot delete the last nadir
            //if(!IsIndexIn_Nadirs && !(IndexIn_PeakIndexMax_PeakIndexMin == this.getNumberOfMaxPeaks()))
            if(IndexIn_PeakIndexMax_PeakIndexMin != this.getNumberOfMaxPeaks())
            {
                if(this.UserAddedNadir_Index != null)
                    this.UserAddedNadir_Index.remove(this.peakIndex_Min.get(IndexIn_PeakIndexMax_PeakIndexMin));
                if(this.UserAddedPeak_Index!=null)
                {
                    this.UserAddedPeak_Index.remove(this.peakIndex_Max.get(IndexIn_PeakIndexMax_PeakIndexMin));                    
                }
                this.peakIndex_Max.remove(IndexIn_PeakIndexMax_PeakIndexMin);
                this.peakValue_Max.remove(IndexIn_PeakIndexMax_PeakIndexMin);  
                this.peakIndex_Min.remove(IndexIn_PeakIndexMax_PeakIndexMin);
                this.peakValue_Min.remove(IndexIn_PeakIndexMax_PeakIndexMin);
                               
                this.ComputeSpikeWidthAndAMP2();
                this.computeISI();
            }
            
        }
    }
    
   
    
    public boolean computeISI()
    {
        if(this.IsPeakFound)
        {
            this.getMaxPeakTime();
            this.computePeakTime();
            if(MaxPeakTime.length>1)
            {                
                this.ISI = Utils.diffArray(this.MaxPeakTime);
                
                return true;
            }
            else
            {   
                if(this.peakIndex_Max.size()>0 && this.SpikeTimeEnd!= null && this.SpikeTimeEnd.length==1)
                {
                    this.ISI = new double[1];
                    this.ISI[0] = this.SpikeTimeEnd[0] - this.SpikeTimeStart[0];
                    return true;
                }
                else
                {
                    this.ISI = null;
                    return false;
                }
            }            
        }
        else
        {
            this.ISI = null;
            return false;
        }
    }
    
    
    public double computeTav()
    {            
        if(this.ISI!=null && this.ISI.length>0)
        {
            this.Tav = Utils.getAverage(this.ISI); 
            return this.Tav;
        }
        else
            
           return 0;       
    }
    
    public double computeSigmaISIs()
    {
        if(this.ISI!=null && this.ISI.length>1)
        {
            this.STD = Utils.getSD(this.ISI); 
            return this.STD;
        }
        else
            return -1;
       
    }
    
    private void findLocalPeaks()
    {
        int size = (this.section_value.size()/2)+1;
        int[] localMinimumIndex = new int[size];
        double[] localMinimumValue = new double[size];
        int[] localMaximumIndex = new int[size];
        double[] localMaximumValue = new double[size];
        
        int indexMax = 0;
        int indexMin = 0;
        this.IsPeakFound = false;
        
        for(int i=1; i<this.section_value.size()-1;i++)
        {
            if(this.section_value.get(i) > this.section_value.get(i-1) && this.section_value.get(i) >= this.section_value.get(i+1))
            {
                localMaximumIndex[indexMax] = i;
                localMaximumValue[indexMax] = this.section_value.get(i);
                indexMax++;
                continue;
            }
            
            if(this.section_value.get(i) <= this.section_value.get(i-1) && this.section_value.get(i) < this.section_value.get(i+1))
            {
                localMinimumIndex[indexMin] = i;
                localMinimumValue[indexMin] = this.section_value.get(i);
                indexMin++;
                
            }            
        }
        this.peakIndex_Min = null;
        this.peakValue_Min = null;
        this.UserAddedNadir_Index = null;
        this.peakIndex_Min = new ArrayList<>();  
        this.peakValue_Min = new ArrayList<>();

        for(int i=0;i<indexMin; i++)
        {
            this.peakIndex_Min.add(localMinimumIndex[i]);
            this.peakValue_Min.add(localMinimumValue[i]);
        }
        this.peakIndex_Max = null;
        this.peakValue_Max = null;
        this.UserAddedPeak_Index = null;
        this.peakIndex_Max = new ArrayList<>();
        this.peakValue_Max = new ArrayList<>();

        for(int i=0;i<indexMax; i++)
        {
            this.peakIndex_Max.add(localMaximumIndex[i]);
            this.peakValue_Max.add(localMaximumValue[i]);            
        }  
        
        localMinimumIndex = null;
        localMinimumValue = null;
        localMaximumIndex = null;
        localMaximumValue = null;
        //System.gc();
    }
    
    public boolean findPeaks()
    {
        findLocalPeaks();
        
        if (peakIndex_Min.size() > 2 && peakIndex_Max.size() > 2)// if there is more than 2 peaks
        {
            if (this.peakIndex_Max.get(0) < this.peakIndex_Min.get(0)) // if signal starts with a max 
            {// remove it, because signal should be start with a min
                this.peakIndex_Max.remove(0);
                this.peakValue_Max.remove(0);
            }
            int lastElement_Index = peakIndex_Max.size() - 1;
            if (this.peakIndex_Max.get(lastElement_Index) > this.peakIndex_Min.get(peakIndex_Min.size() - 1)) {
                // signal should be ended with a minimum
                this.peakIndex_Max.remove(lastElement_Index);
                this.peakValue_Max.remove(lastElement_Index);
            }

            double[] OscilUp = new double[peakIndex_Max.size()];
            double[] OscilDown = new double[peakIndex_Max.size()];
            double[] UpDownMean = new double[peakIndex_Max.size()];

            for (int i = 0; i < peakIndex_Max.size(); i++) {
                OscilUp[i] = (this.peakValue_Max.get(i) - this.peakValue_Min.get(i));
                OscilDown[i] = (this.peakValue_Max.get(i) - this.peakValue_Min.get(i + 1));

                UpDownMean[i] = (OscilUp[i] + OscilDown[i]) / 2;
            }

            double MaxUp = Utils.findMax(OscilUp);
            double MaxDown = Utils.findMax(OscilDown);

            double Max_amplitude = 0;
            if (MaxUp > MaxDown) {
                Max_amplitude = MaxUp;
            } else {
                Max_amplitude = MaxDown;
            }

            double Peak_Threshold = (this.PeakPercent / 100) * Max_amplitude;

            boolean[] IsPeak = new boolean[peakIndex_Max.size()];
            ArrayList<Integer> PeakIndex = new ArrayList<>();
            //this array just contaiin the Index of global peaks
            int count = 0;
            for (int i = 0; i < peakIndex_Max.size(); i++) {
                if (UpDownMean[i] >= Peak_Threshold) {
                    IsPeak[i] = true;
                    PeakIndex.add(i); //this array keep index of peaks that 
                    //are registered in the peakIndex_Max array as a local peak but now they are recognized
                    //as a global peak
                    count++;
                }
            }
            int i = 0;
            double DownUpMean = 0;
            while (i < (count - 1)) {
                //PeakIndex array contains indexes of peakIndex_Max that are identified as global peak
                if (PeakIndex.get(i+1) == PeakIndex.get(i) +1){
                    // if two subsequent local peaks, saved in peakIndex_Max array, are identified as 2 global peaks, 
                    //it would be possible that just one of them should be identified as 
                    // a global peak. since there are ususally some loacl peaks between global paeks.
                    //So we check the minimum point between them.
                                      
                    int IndexDown = PeakIndex.get(i); //index of peak which is before the minimum
                    int IndexUp = PeakIndex.get(i + 1); //index of peak which is after the minimum
                    //the minimum point is located beween IndexDown and IndexUp
                    if(OscilDown[IndexDown] <= 0.5*OscilUp[IndexDown] || OscilUp[IndexUp] <= 0.5*OscilDown[IndexUp])
                    {
//                    DownUpMean = (OscilDown[IndexDown] + OscilUp[IndexUp]) / 2;
//                    if (DownUpMean < Peak_Threshold) {
                        if (this.peakValue_Max.get(IndexDown) > this.peakValue_Max.get(IndexUp)) {
                            IsPeak[IndexUp] = false;
                            
                        } else {
                            IsPeak[IndexDown] = false;
                            // now we can change the minimum of the later peak, to the less minimum.
                            if(this.peakValue_Min.get(IndexDown) < this.peakValue_Min.get(IndexUp)){
                               
                                this.peakValue_Min.set(IndexUp,this.peakValue_Min.get(IndexDown));
                                this.peakIndex_Min.set(IndexUp,this.peakIndex_Min.get(IndexDown));
                            }
                        }
                                               
                    }
                }
                i = i + 1;
            }
            int Index = 0;
            for(int j=0; j<IsPeak.length; j++)
            {
               if(!IsPeak[j])
                {
                   this.peakIndex_Max.remove(Index);
                   this.peakValue_Max.remove(Index);
                   this.peakIndex_Min.remove(Index);
                   this.peakValue_Min.remove(Index);
                }
               else
               {
                  Index++; 
               }
            }
            
            boolean[] IsPeak2 = new boolean[peakIndex_Max.size()];
            
            for(int j=0; j<peakIndex_Max.size();j++ )
            {
                IsPeak2[j] =  ((peakValue_Max.get(j) - peakValue_Min.get(j)) >= Peak_Threshold) &&
                                ((peakValue_Max.get(j) - peakValue_Min.get(j+1)) >= Peak_Threshold);
            }
            
            Index = 0;
            for(int j=0; j<IsPeak2.length; j++)
            {
               if(!IsPeak2[j])
                {
                   this.peakIndex_Max.remove(Index);
                   this.peakValue_Max.remove(Index);
                   this.peakIndex_Min.remove(Index);
                   this.peakValue_Min.remove(Index);
                }
                else
                    Index++;
            }
            if(this.peakIndex_Max.isEmpty())
            {
               this.peakIndex_Min.clear();
               this.peakValue_Min.clear();
               this.IsPeakFound = false;
            }
            else
            {               
                refineMinPeaks();                
                if(this.ComputeSpikeWidthAndAMP2())
                {
                    this.IsPeakFound = true;
                    this.computeISI();
                }
                else
                {
                   this.IsPeakFound = false;
                   this.peakIndex_Max.clear();
                   this.peakValue_Max.clear();
                   this.peakIndex_Min.clear();
                   this.peakValue_Min.clear();
                }  
               
                
               
            }
                        
            this.STD = 0;
            this.Tav = 0;
            OscilUp = null;
            OscilDown = null;
            UpDownMean = null;
            IsPeak = null;
            PeakIndex = null;
            IsPeak2 = null;
        }
        else
            this.IsPeakFound = false;
        
        return this.IsPeakFound;       
    }
    
    public void refineMinPeaks() {
        if (this.peakIndex_Max != null && this.peakIndex_Max.size() > 2) {
            int IndexStart = 0;
            int IndexEnd = 0;
            double localMinAfterPeak = 0;
            int localMinAfterPeak_Index = 0;
            boolean localMinAfterPeak_IsFound = false;
            boolean PossibleToChangeNadir = false;

            double localMinBeforePeak = 0;
            int localMinBeforePeak_Index = 0;
            //the local minimum before each peak may need to be refined
            for (int i = 1; i < this.peakIndex_Max.size() - 1; i++) {
                //if nadir is not added by user, software can change its location
                if (this.UserAddedNadir_Index == null) {
                    PossibleToChangeNadir = true;
                } else {
                    if (this.UserAddedNadir_Index.contains(this.peakIndex_Min.get(i))) {
                        PossibleToChangeNadir = false;
                    } else {
                        PossibleToChangeNadir = true;
                    }
                }
                if (PossibleToChangeNadir) {
                    //find the local minimum which happens after this peak
                    IndexStart = this.peakIndex_Max.get(i);
                    
                    IndexEnd = (int) ((0.5) * (this.peakIndex_Max.get(i + 1) - this.peakIndex_Max.get(i))) + IndexStart;
                
                    localMinAfterPeak = this.peakValue_Max.get(i);

                    for (int j = IndexStart; j < IndexEnd; j++)//should be find in this regon.
                    {
                        if (this.section_value.get(j) < localMinAfterPeak) {
                            localMinAfterPeak = this.section_value.get(j);
                            localMinAfterPeak_Index = j;
                            localMinAfterPeak_IsFound = true;
                        }
//                    else
//                    {
//                        localMinAfterPeak_IsFound = true; // first minimum peak after this peak is found
//                        break;
//                    }
                    }
                    if (localMinAfterPeak_IsFound) {
                        IndexStart = this.peakIndex_Max.get(i) - (int) (0.5 * (this.peakIndex_Max.get(i) - this.peakIndex_Max.get(i - 1)));

                        //IndexStart = this.peakIndex_Max.get(i-1);
                        IndexEnd = this.peakIndex_Max.get(i);

//                        localMinBeforePeak_Index = this.peakIndex_Min.get(i);
//                        localMinBeforePeak = this.peakValue_Min.get(i);
                        
                        localMinBeforePeak_Index = this.peakIndex_Max.get(i);
                        localMinBeforePeak = this.peakValue_Max.get(i);

                        double compareY_min = Math.abs(localMinAfterPeak - localMinBeforePeak);
                        boolean NewMinimumIsFound = false;

                        for (int j = IndexEnd; j >= IndexStart; j--) //find local minimums in this window
                        {
                            if (this.section_value.get(j) < localMinBeforePeak) {
                                //if (Math.abs(localMinAfterPeak - this.section_value.get(j)) < compareY_min) {
                                    localMinBeforePeak_Index = j;
                                    localMinBeforePeak = this.section_value.get(j);
                                    //compareY_min = Math.abs(localMinAfterPeak - localMinBeforePeak);
                                    NewMinimumIsFound = true;
                                //}
                            }
                        }
                        if (NewMinimumIsFound) //update the minimum
                        {
                            this.peakIndex_Min.set(i, localMinBeforePeak_Index);
                            this.peakValue_Min.set(i, localMinBeforePeak);
                        }
                    } else {
                        break;
                    }
                }

            }
        }
        if (this.peakIndex_Max != null && this.peakIndex_Max.size() > 0) //define the last nadir
        {
            for (int i = this.peakIndex_Max.get(this.peakIndex_Max.size() - 1); i < this.section_time.size() - 1; i++) {
                if (this.section_value.get(i) < this.section_value.get(i + 1)) //&& this.section_value.get(i)<=this.peakValue_Min.get(this.peakValue_Max.size()-1))
                {
                    this.peakIndex_Min.set(this.peakIndex_Min.size() - 1, i);
                    this.peakValue_Min.set(this.peakValue_Min.size() - 1, this.section_value.get(i));
                    break;
                }
            }
        }
    }
    
    public boolean CheckIfTwoSectionsHaveConfilict(ArrayList<Double> NewTimeRange)
    {
        boolean NoConflict = true;
        double MinTimeSec1;
        double MinTimeSecNew;
        double MaxTimeSec1;
        double MaxtimeSecNew;
        if(this.section_time.size()>0)
        {
            MinTimeSec1 = Utils.findMin(this.section_time);
            MinTimeSecNew = Utils.findMin(NewTimeRange);

            MaxTimeSec1 = Utils.findMax(this.section_time);
            MaxtimeSecNew = Utils.findMax(NewTimeRange);
            
            if(MinTimeSecNew<MaxtimeSecNew)
            {
                if(MinTimeSecNew > MaxTimeSec1 || MaxtimeSecNew < MinTimeSec1)
                {
                    NoConflict = true;
                }
                else
                    NoConflict = false;
            }
            else
                NoConflict = false;
        }
       
        return  NoConflict;
           
    }
    
    public int getPeakNumber()
    {
        if(this.peakIndex_Max!=null)
            return this.peakIndex_Max.size();
        else
            return 0;
    }
    
    public boolean HasMoreThanOnePeak()
    {
        return this.getPeakNumber()>1;
    }
    
    public double getStartTime()
    {
        if(this.section_time!=null)
        {
            return this.section_time.get(0);
        }
        else return 0;
    }
    
    public double getEndTime()
    {
        if(this.section_time!=null){
          return this.section_time.get(section_time.size()-1);
        }
        else return 0;
    }
    
    public double getPeakThr()
    {
        return this.PeakPercent;
    }
    
}
