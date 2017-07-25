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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mahsa.moein
 */
public class Experiment {

    public String Path_of_signals;

    private String FileNameContains;

    private String SignalColNameContains;

    private float TimeBetSamples;
    
    private int TimeColumnNumber;
    
    public boolean SigmaTavShown;

    public ArrayList<Signal> signals;
    
    public ArrayList<Double> Origin_Exp_time;
    
    private FrmSigAnalysis signalAnalysisForm;
    
    public ArrayList<Integer> DeletedSignalsList; 
    
    public ArrayList<Integer> FilteredSignalsList; 
       
    public PlotPoints SigmaTavPlot;
    
    public boolean ExperimentHasTreatment;
    
    public int NumberOfSections;
    
    public ArrayList<String> NameOfSections;
    
    public double[] Sigma_Tav_Slope;
    public double[] Sigma_Tav_Intercept;
    public double[] RMSE; 
    public double[] Mean_Tav;
    
    private ArrayList<StdTav> STD_Tav_Datasets;
    
    
    
    public Experiment(FrmSigAnalysis saf)
    {
        this.signalAnalysisForm = saf;
        this.DeletedSignalsList = new ArrayList();
        this.FilteredSignalsList = new ArrayList();
        this.NumberOfSections = 0;
    }
    
    public String getPath_of_signals() {
        return Path_of_signals;
    }

    public void setPath_of_signals(String Path_of_signals) {
        this.Path_of_signals = Path_of_signals;
    }

    public String getFileNameContains() {
        return FileNameContains;
    }

    public void setFileNameContains(String FileNameContains) {
        this.FileNameContains = FileNameContains;
    }

    public String getSignalColNameContains() {
        return SignalColNameContains;
    }

    public void setSignalColNameContains(String SignalColNameContains) {
        this.SignalColNameContains = SignalColNameContains;
    }

    public float getTimeBetSamples() {
        return TimeBetSamples;
    }

    public void setTimeBetSamples(float TimeBetSamples) {
        this.TimeBetSamples = TimeBetSamples;       
    }
    
    public void EditTimeBetSamples(float newTimeBetSamples){
       for (Signal sig : this.signals) {
          sig.EditSignalTimeFactor( this.TimeBetSamples, newTimeBetSamples);           
       }
       
       this.TimeBetSamples = newTimeBetSamples;
    }
    
     public int getTimeColumnNumber() {
        return TimeColumnNumber;
    }

    public void setTimeColumnNumber(int TimeColumnNumber) {
        this.TimeColumnNumber = TimeColumnNumber;
    }
    
    public int total_signals_in_Allfiles;
    
    public boolean Isfilled_Origin_Exp_time;
   
    
    public void AddSectionToSignals(int numberOfSections, ArrayList<String> SectionNames)
    {
        for (Signal sig : this.signals) { // all signals have the same number and name of sections
           
            sig.AddMultiSection(numberOfSections,SectionNames);           
        }
        
        this.NumberOfSections = numberOfSections;
        this.NameOfSections = SectionNames;
        this.Sigma_Tav_Slope = new double[numberOfSections];
        this.Sigma_Tav_Intercept = new double[numberOfSections];
        this.RMSE = new double[numberOfSections];
        this.Mean_Tav = new double[numberOfSections];
    }
    
    public void AddOneSectionToEachSignal()
    {
       for (Signal sig : this.signals) {
           sig.AddOneSection();
       } 
       
        this.NumberOfSections = 1; // whole signal is one section
        this.NameOfSections = null;
        this.Sigma_Tav_Slope = new double[1];
        this.Sigma_Tav_Intercept = new double[1];
        this.RMSE = new double[1];
        this.Mean_Tav = new double[1];
    }
    
    public boolean AddTimeAndValueToSections(double timeFrom, double timeTo, int SectionID)
    {
        boolean result = true;
        for (Signal sig : this.signals) {
            if(!sig.IsDeleted())
            {
                result = result && sig.AddTimeAndValueToSections(timeFrom, timeTo, SectionID);
            }
        }
        return result;
    }
    
    public boolean IsTimeAddedToAllSections()
    {
       boolean result = true;
        for (Signal sig : this.signals) {
            result = result && sig.IsTimeAddedToAllSections();
        }
        return result;
    }
    
    public Utils.ReadFileStatus ReadSignals() throws InvalidFormatException, IOException
    {
        this.signals= null;
        this.signals = new ArrayList<>();
        this.Origin_Exp_time = new ArrayList<>();
        
        this.total_signals_in_Allfiles = 0;
        this.Isfilled_Origin_Exp_time = false;  
        
        File file = new File(Path_of_signals);        
        String[] names = file.list();
        file = null;
        
        
        Utils.ReadFileStatus ReadStatus = Utils.ReadFileStatus.FileIsReadCorrectly;
        
        for (String Filename : names) {
            
            String extension = Utils.getExtension(Filename);
            String fName = Utils.getFlieName_WithoutExt(Filename);
            String File_Path = Path_of_signals + "/" + Filename;
            
            if ((extension.equals(Utils.xls))
                    && fName.toLowerCase().contains(FileNameContains.toLowerCase())) {
                                
               ReadSignalsExcel(File_Path);
            }
            else{
                if ((extension.equals(Utils.cvs) || (extension.equals(Utils.txt)) || (extension.equals(Utils.dat)) || (extension.equals(Utils.csv)))
                    && fName.toLowerCase().contains(FileNameContains.toLowerCase())) {
                    
                   ReadStatus = Utils.CheckFileValidity(File_Path);
                   if(ReadStatus != Utils.ReadFileStatus.InputFileDoesNotHaveHeader)
                   {
                        ReadSignalsCVS(File_Path);
                   }
                   else
                   {
                       return ReadStatus;
                   }
                }
            }
        }
       
        if(this.signals.isEmpty())
        {
            this.signals= null;
            ReadStatus = Utils.ReadFileStatus.EmptySiganlArray;
        }
        else 
        {              
            int i = 0;
            while (i < this.signals.size()) {
                if(!this.signals.get(i).Origin_sig_time.isEmpty())
                {
                    if (this.signals.get(i).Origin_sig_value.size() == this.signals.get(i).Origin_sig_time.size()) {
                        this.signals.get(i).CopyInitialSignal();
                        i++;
                    } else {
                        this.signals.remove(i);
                    }                    
                }
                else
                {
                    if (!this.Origin_Exp_time.isEmpty()) { // if at least one of the files has time column, This column will be set a
                        if (this.signals.get(i).Origin_sig_value.size() == this.Origin_Exp_time.size()) {
                            for(int j=0; j< Origin_Exp_time.size();j++)
                            {
                               this.signals.get(i).Origin_sig_time.add(Origin_Exp_time.get(j)); 
                            }
                            this.signals.get(i).CopyInitialSignal();
                            i++;
                        } else {
                            this.signals.remove(i);                            
                        }
                       
                    }
                    else
                    {
                        if(this.TimeColumnNumber == 0)
                        {
                            double time = 1.0;
                            for(int j=0 ; j<this.signals.get(i).Origin_sig_value.size(); j++)
                            {
                                this.signals.get(i).Origin_sig_time.add(time);
                                time = time + 1;
                            }
                            this.signals.get(i).CopyInitialSignal();
                            i++;
                        }
                    }
                    
                    if(this.signals.isEmpty())
                    {
                       ReadStatus = Utils.ReadFileStatus.NotEqualNumbersForSignalandTime;
                    }
                }
            }
            
        }
        return ReadStatus;        
    }

    private boolean ReadSignalsExcel(String File_Path) throws InvalidFormatException {
    
        FileInputStream fis = null;
        ArrayList<Signal> signals_InOne_file = null;
        ArrayList<Double> signalTime_InOne_file = null;
        Workbook workbook = null;
        //org.apache.poi.ss.usermodel.Workbook workbook = null;
        //String File_Path = Path_of_signals + "/" + Filename;
        boolean ExceptionHappened = false;
        boolean EXPtime_IsFilled_from_this_File = false;
        try {
            // read the excel file
            fis = new FileInputStream(File_Path);
            signals_InOne_file = new ArrayList<>();
            signalTime_InOne_file = new ArrayList<>();
            //Create Workbook instance for xlsx/xls file input stream
            
//            if (Filename.toLowerCase().endsWith("xlsx") ) {
//                workbook = new XSSFWorkbook(fis);
                 //workbook = WorkbookFactory.create(fis);
//            } else if (Filename.toLowerCase().endsWith("xls")) {
//                workbook = new HSSFWorkbook(fis);
//            }
            
           // workbook = WorkbookFactory.create(fis);
            workbook = new HSSFWorkbook(fis);
            int NumberOfSignalsInOneFile = 0;
            //Get the nth sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

            //every sheet has rows, iterate over them
            Iterator<Row> rowIterator = sheet.iterator();
            boolean IsHeader = true;
            int Time_columnNo = this.TimeColumnNumber-1;// time column should be same in all experiment files.
            int rowCounter = 0;
            boolean ColumnIsTime = false;
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                short lastCellNum = row.getLastCellNum();
                rowCounter++;
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                //int columnCounter = 0;
                
                while (cellIterator.hasNext()) { // cells which are empty, are not read!                    
                    Cell cell = cellIterator.next();
                    int col = cell.getColumnIndex();
                    //columnCounter++;
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) { 

                        case Cell.CELL_TYPE_STRING: {//Find the column number of signals_InOne_file and time
                            if (IsHeader) {
                                if (cell.getStringCellValue().toLowerCase().contains(SignalColNameContains.toLowerCase()) && col != Time_columnNo) {
                                    NumberOfSignalsInOneFile++;
                                    total_signals_in_Allfiles++;
                                    //Signal s = new Signal(total_signals_in_Allfiles, columnCounter);
                                    Signal s = new Signal(total_signals_in_Allfiles, col,cell.getStringCellValue());
                                    signals_InOne_file.add(s);
                                } 

                            }
                          break;
                        }

                        case Cell.CELL_TYPE_NUMERIC: {
                            if (!IsHeader) {
                                boolean Cell_Is_signal = false;
                                for (int i = 0; i < NumberOfSignalsInOneFile; i++) {
                                    //if (columnCounter == signals_InOne_file.get(i).getColumnNo()) {
                                    if (col == signals_InOne_file.get(i).getColumnNo()) {
                                        signals_InOne_file.get(i).Origin_sig_value.add(cell.getNumericCellValue());
                                        Cell_Is_signal = true;
                                        break;
                                    }
                                }
                                if (!Cell_Is_signal) // maybe cell is time 
                                {
                                    //if (columnCounter == Time_columnNo) { // cell is time
                                    if (col == Time_columnNo) { // cell is time    
                                        if (!Isfilled_Origin_Exp_time) { // the first time column in first file is set as time of experiment
                                            if (this.Origin_Exp_time.isEmpty()) {
                                                this.Origin_Exp_time.add(cell.getNumericCellValue());    //first value should not multiply 
                                                 EXPtime_IsFilled_from_this_File = true;
                                            } else {
                                                this.Origin_Exp_time.add(cell.getNumericCellValue() * this.getTimeBetSamples());
                                                
                                            }                                                                                    
                                        }
                                                                             
                                        if (signalTime_InOne_file.isEmpty() && !EXPtime_IsFilled_from_this_File) { // if every file has its own time column

                                            if (cell.getNumericCellValue() == 1) {
                                                signalTime_InOne_file.add(cell.getNumericCellValue());
                                                ColumnIsTime = true;
                                            } else {
                                                ColumnIsTime = false;
                                            }
                                        } else {
                                            
                                            if(ColumnIsTime)
                                            {
                                               signalTime_InOne_file.add(cell.getNumericCellValue() * this.getTimeBetSamples());
                                            }
                                        }

                                    }
                                }
                            }
                            break;
                        }

                    }
                }

                IsHeader = false; // after reading the first row of excel file
            }

            fis.close();// one excel file is read. we have the signals_InOne_file and the time.
            // we should now assign signals to experiment

            if (!this.Origin_Exp_time.isEmpty())// the whole experiment have just one time column
            {
                Isfilled_Origin_Exp_time = true;
            }
            if(!signalTime_InOne_file.isEmpty()) // if each file has its own time, then use it.
            {
                for (Signal sig : signals_InOne_file) {
                    
                    for(int i=0; i<signalTime_InOne_file.size();i++)
                    {
                        sig.Origin_sig_time.add(signalTime_InOne_file.get(i));
                    }
                }
            }
            for (Signal signals_InOne_file1 : signals_InOne_file) {

                this.signals.add(signals_InOne_file1);

            }                        
            signals_InOne_file = null; 
            
        } 

        catch (FileNotFoundException e) {
        } catch (IOException e) {
                 ExceptionHappened = true;
                
        }
        finally{
            signals_InOne_file = null;
            signalTime_InOne_file = null;
            fis = null;
        }
        return ExceptionHappened;

    }   
    
    private boolean ReadSignalsCVS(String File_Path) {// read signals in one file
           
        ArrayList<Signal> signals_InOne_file = null;
        ArrayList<Double> signalTime_InOne_file = null;

       // String File_Path = Path_of_signals + "/" + Filename;

        BufferedReader br = null;
        boolean ExceptionInReading = false;

        try {

            signals_InOne_file = new ArrayList<>();
            signalTime_InOne_file = new ArrayList<>();
            String sCurrentLine;

            br = new BufferedReader(new FileReader(File_Path));

            int rowCounter = 0;
            int NumberOfSignalsInOneFile = 0;
            boolean IsHeader = true;
            int Time_columnNo = this.TimeColumnNumber;// time column number should be same in all experiment files
            int NumberOfColumns = 0;
            String Split = "";
            boolean ColumnIsTime = false;
            boolean EXPtime_IsFilled_from_this_File = false;
            try{
            while ((sCurrentLine = br.readLine()) != null) {// read file row by row

                //String[] parts = sCurrentLine.split(",");
                String[] parts = null;
                if (IsHeader) {
//                    if (sCurrentLine.split(";").length > 1) {
//                        parts = sCurrentLine.split(";");
//                        Split = ";";
//                    } else 
//                    
                    if (sCurrentLine.split(",").length >1) {
                        parts = sCurrentLine.split(",");
                        Split = ",";
                    } else if (sCurrentLine.split("\t").length >1) {
                    parts = sCurrentLine.split("\t");
                    Split = "\t";
                    } else {
                        if(parts == null)
                        {
                            if(sCurrentLine != null)
                            {
                               parts = new String[1];
                               parts[0] =  sCurrentLine;
                               Split = ","; // just as a value       
                            }
                            else
                                break;
                        }                       
                    }
                    NumberOfColumns = parts.length;
                } else {
                    if (NumberOfColumns != 0) {
                        parts = sCurrentLine.split(Split);
                    } else {
                        break;
                    }
                }

                rowCounter++;
                
                //For each row, iterate through all the columns
                System.out.print("\n");
                System.out.print(" Row Number is : ");
                System.out.print(rowCounter);
                
                for (int columnCounter = 0; columnCounter < NumberOfColumns; columnCounter++) {
                    System.out.print(" Column Number is : ");
                    System.out.print(parts[columnCounter]);
                    
                    if (!Utils.IsDataNumeric(parts[columnCounter])) // if data is not numeric, then it is string
                    {
                        if (IsHeader) {//Find the column number of signals_InOne_file and time

                            if (String.valueOf(parts[columnCounter]).toLowerCase().contains(SignalColNameContains.toLowerCase())) {

                                NumberOfSignalsInOneFile++;
                                total_signals_in_Allfiles++;
                                Signal s = new Signal(total_signals_in_Allfiles, columnCounter,String.valueOf(parts[columnCounter]));
                                signals_InOne_file.add(s);
                            }
                        }
                    } else // data is numeric
                    {
                        if (!IsHeader) {
                            boolean Cell_Is_signal = false;
                            for (int i = 0; i < NumberOfSignalsInOneFile; i++) {
                                if (columnCounter == signals_InOne_file.get(i).getColumnNo()) {
                                    signals_InOne_file.get(i).Origin_sig_value.add(Double.parseDouble(String.valueOf(parts[columnCounter])));
                                    Cell_Is_signal = true;
                                    break;
                                }
                            }
                            if (!Cell_Is_signal) // maybe data is time 
                            {
                                if (columnCounter+1 == Time_columnNo) { // data is time
                                    //because columnCounter starts from zero but columns in file nmebrs from 1.

                                    if (!this.Isfilled_Origin_Exp_time) {// time from first file is set as time of experiment
                                        if (this.Origin_Exp_time.isEmpty()) { // first row of Time column
                                            this.Origin_Exp_time.add(Double.parseDouble(String.valueOf(parts[columnCounter])));
                                            EXPtime_IsFilled_from_this_File = true;
                                        } else {
                                            this.Origin_Exp_time.add(Double.parseDouble(String.valueOf(parts[columnCounter])) * this.getTimeBetSamples());
                                        }
                                        
                                    }
                                   
                                    if (signalTime_InOne_file.isEmpty() && !EXPtime_IsFilled_from_this_File) {

                                        if (Double.parseDouble(String.valueOf(parts[columnCounter])) == 1 && rowCounter==2) { // if the first value of time is one, then the column is time

                                            signalTime_InOne_file.add(Double.parseDouble(String.valueOf(parts[columnCounter])));
                                            ColumnIsTime = true;

                                        } else {
                                            ColumnIsTime = false;
                                        }
                                    } else {

                                        if (ColumnIsTime) {
                                            signalTime_InOne_file.add(Double.parseDouble(String.valueOf(parts[columnCounter])) * this.getTimeBetSamples());
                                        }
                                    }
                              
                            }
                        }

                    }
                }            
            }            
            IsHeader = false; // after reading the first row of excel file            
            }
            }
            catch(Exception e)
            {
               ExceptionInReading = true;
               return ExceptionInReading;
            }
        // one cvs file is read. we have the signals_InOne_file and the time.
            // we should now assign signals to experiment
            if (!this.Origin_Exp_time.isEmpty())// the whole experiment have just one time column
            {
                this.Isfilled_Origin_Exp_time = true;
            }
            
            if(!signalTime_InOne_file.isEmpty())
            {
                for (Signal sig : signals_InOne_file) {
                    
                    for(int i=0; i<signalTime_InOne_file.size();i++)
                    {
                        sig.Origin_sig_time.add(signalTime_InOne_file.get(i));
                    }
                }
            }

            for (Signal signals_InOne_file1 : signals_InOne_file) {

                this.signals.add(signals_InOne_file1);

            }
        

            //signals_InOne_file = null;            

        } catch (IOException e) {
            ExceptionInReading = true;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                signals_InOne_file = null;
                signalTime_InOne_file = null;
                
                
            } catch (IOException ex) {
                ExceptionInReading = true;
            }
        }
        return ExceptionInReading;

    }
    
    
    public boolean EditExp(double timeFrom, double timeTo)
    {
        boolean result = true;           
        for (Signal sig : this.signals) {

            //result = result && (sig.EditSignalWithOriginalSignal(timeFrom, timeTo,normalizeSignals));
            result = result && (sig.EditSignal(timeFrom, timeTo));

        }
        return result;               
    }
    
    public void ResetExpSignals()
    {        
        for (Signal sig : this.signals) {                
                sig.ResetSignal();
                sig.createSignalPlot(this);
        }       
    }
    
    public void NormalizeWholeSignals()
    {    
//        for (Signal sig : this.signals) {                
//                sig.NormalizeSignal();
//         }       
    }
    
    public void findPeaks(double peakThreshold)
    {
        for (Signal sig : this.signals) {
            sig.findPeaks(peakThreshold);
            //sig.createPeakPlot(this,sigPlot); 
        }        
    }
    
    public void findPeaks(double peakThreshold, int SectionID)
    {
        for (Signal sig : this.signals) {
            sig.findPeaks(peakThreshold,SectionID);
           // sig.createPeakPlot(this,sigPlot); 
        }        
    }
        
    public void setSelectedPointsInLabels(Signal sig,int selectedIndex,String ComponentId, int SectionId)//, int X, int Y)
    {
        //selected index is the index of clicked point in its dataset.
        if(ComponentId.contains("signal"))
        {
            this.signalAnalysisForm.SetjLblSelectedTimeValue(sig,sig.sig_time.get(selectedIndex));
            this.signalAnalysisForm.SetjLblSelectedSignalValue(sig,sig.sig_value.get(selectedIndex));
            sig.setSelectedSectionAndIndex(SectionId,selectedIndex,true,false,false); // section ID is always zero here
            //selectedIndex is an index in signal series
        }
        if(ComponentId.contains("MaxPeaks"))
        {
            int IndexInSection = sig.sections.get(SectionId).peakIndex_Max.get(selectedIndex);
            this.signalAnalysisForm.SetjLblSelectedTimeValue(sig,sig.sections.get(SectionId).section_time.get(IndexInSection));
            this.signalAnalysisForm.SetjLblSelectedSignalValue(sig,sig.sections.get(SectionId).section_value.get(IndexInSection));
            sig.setSelectedSectionAndIndex(SectionId,IndexInSection,false,true,false);
        }
        if(ComponentId.contains("MinPeaks"))
        {
            int IndexInSection  = sig.sections.get(SectionId).peakIndex_Min.get(selectedIndex);
            this.signalAnalysisForm.SetjLblSelectedTimeValue(sig,sig.sections.get(SectionId).section_time.get(IndexInSection));
            this.signalAnalysisForm.SetjLblSelectedSignalValue(sig,sig.sections.get(SectionId).section_value.get(IndexInSection));
            sig.setSelectedSectionAndIndex(SectionId,IndexInSection,false,false,true);
        }
    }
    
    public void setEditSignalBoxes(double From, double To,boolean FillTextBoexs)
    {
        this.signalAnalysisForm.setEditSignalBoxes(From, To,FillTextBoexs);
    }
    
    public void AddSelectedPointAsPeak()
    {
        this.signalAnalysisForm.AddSelectedPeak();
    }
    
    public void AddSelectedPointAsNadir()
    {
        this.signalAnalysisForm.AddSelectedNadir();
    }
    public void RemovePeakorNadir()
    {
        this.signalAnalysisForm.RemoveSelectedPeakorNadir();
    }
    
    public int countUnDeletedUnFilteredSignals()
    {
        return this.signals.size() - (this.DeletedSignalsList.size() + this.FilteredSignalsList.size());
    }
    
    public void FilterSignals(int PeakNo, int SectionID)
    {      
      
        ArrayList<Integer> tempStore = new ArrayList();
        boolean SectionIsAlreadyFiltered = false;
        for (Signal sig : this.signals){
            
           //boolean SectionIsAlreadyFiltered = false;
           if(!sig.IsDeleted() && sig.sections.get(SectionID).IsPeakFound) 
           {
               boolean SignalIsFound = false;
               for(int i=0; i< this.FilteredSignalsList.size(); i++)
               {
                   if(this.FilteredSignalsList.get(i) == sig.getSig_Id())
                   {
                       SignalIsFound = true;
                       SectionIsAlreadyFiltered = false;
                       for (Sections section : sig.sections) {

                           if(section.IsFiltered && section.getSecID() == SectionID)
                           {
                               this.FilteredSignalsList.remove(i); // filter is applied once on this section, then we remove this signal from the list for applying the new filter
                               SectionIsAlreadyFiltered = true;
                               sig.sections.get(SectionID).IsFiltered = false;
                               sig.SetFilterFlag(false);
                               break;
                           }
                       }
                   }
                   if(SignalIsFound)
                       break;
               }
               if(sig.sections.get(SectionID).peakIndex_Max.size() <= PeakNo )
               {                            
                   if(!SignalIsFound || SectionIsAlreadyFiltered)
                   {
                       tempStore.add(sig.getSig_Id());
                       sig.SetFilterFlag(true);                       
                   }
                   sig.sections.get(SectionID).IsFiltered = true;
                   
               }
           }              
        }
        for (Integer tempStore1 : tempStore) {
            this.FilteredSignalsList.add(tempStore1);
        }
        tempStore = null;
        this.signalAnalysisForm.ShowUnFilteredSignals();
    }
    
    public void RemoveFilter()
    {
        this.FilteredSignalsList.clear();        
        for (Signal sig : this.signals){

            sig.SetFilterFlag(false);
            for(int i=0; i<this.NumberOfSections;i++)
            {
                sig.sections.get(i).IsFiltered = false;
            }
        }
        this.signalAnalysisForm.ShowSignalsRemoveFilter();
        
    }
    
    private void computeTavAndSTD()
    {
       this.STD_Tav_Datasets = null;
        STD_Tav_Datasets = new ArrayList<StdTav>();
        
        for(int i=0; i<this.NumberOfSections; i++)
        {
            int count = 0;
            StdTav StdTavDataset = new StdTav();
            StdTavDataset.SectionID = i;
            for (Signal sig : this.signals){
                
                if(!sig.IsDeleted() && sig.IsPeakFound && !sig.IsFiltered())
                {
                   if(sig.sections.get(i).IsPeakFound && sig.sections.get(i).IsISILengthBiggerOne()) 
                   {
                       
                       StdTavDataset.SignalID.add(sig.getSig_Id());                       
                       StdTavDataset.Tav.add(sig.sections.get(i).computeTav());
                       StdTavDataset.STD.add(sig.sections.get(i).computeSigmaISIs());
                       count++;
                   }
                }
            }            
            STD_Tav_Datasets.add(StdTavDataset);
        }        
        
    }
    
    public boolean plotSigmaTavPlot()
    {
        this.SigmaTavPlot = null;
        this.SigmaTavPlot = new PlotPoints();        
        computeTavAndSTD();
        double minTav = 0;
        double minSTD = 0;
        double maxTav = 0;
        double maxSTD = 0;
        
        boolean primaryMinMaxIsFound = false;
        
        for(int i=0; i<this.NumberOfSections;i++)
        {
            if(STD_Tav_Datasets.get(i).Tav.size()>1)
            {
                minTav = Utils.findMin(STD_Tav_Datasets.get(i).Tav);
                minSTD = Utils.findMin(STD_Tav_Datasets.get(i).STD);

                maxTav = Utils.findMax(STD_Tav_Datasets.get(i).Tav);
                maxSTD = Utils.findMax(STD_Tav_Datasets.get(i).STD);
                primaryMinMaxIsFound = true;
                break;
            }            
        }
        if(primaryMinMaxIsFound)
        {
            for(int i=0; i<this.NumberOfSections;i++)
            {
                if(STD_Tav_Datasets.get(i).Tav.size()>1)
                {
                    if(minTav > Utils.findMin(STD_Tav_Datasets.get(i).Tav))
                    {
                        minTav = Utils.findMin(STD_Tav_Datasets.get(i).Tav);
                    }
                    if(minSTD > Utils.findMin(STD_Tav_Datasets.get(i).STD))
                    {
                       minSTD = Utils.findMin(STD_Tav_Datasets.get(i).STD); 
                    }            
                    if(maxTav < Utils.findMax(STD_Tav_Datasets.get(i).Tav))
                    {
                       maxTav =  Utils.findMax(STD_Tav_Datasets.get(i).Tav);
                    }
                    if(maxSTD < Utils.findMax(STD_Tav_Datasets.get(i).STD))
                    {
                         maxSTD = Utils.findMax(STD_Tav_Datasets.get(i).STD);
                    }
                }
            } 
           
            for(int i=0; i<this.NumberOfSections;i++)
            {
                if(STD_Tav_Datasets.get(i).Tav.size()>1)
                {
                    if(STD_Tav_Datasets.get(i).SortBasedOnTav())
                    {

                        String ComponentId = String.valueOf(STD_Tav_Datasets.get(i).SectionID) + '_' + "STDTav";

                        this.SigmaTavPlot.AddPlot(this,STD_Tav_Datasets.get(i).Tave 
                                ,STD_Tav_Datasets.get(i).Stdev
                                ,ComponentId, minTav, minSTD, maxTav, maxSTD, "Tav(s)", "SD(s)", true,true);
                    }
                   
                }
                else
                {
                    this.Sigma_Tav_Slope[i] = 0;
                    this.RMSE[i] = 0;
                    this.Sigma_Tav_Intercept[i] = 0;
                    this.Mean_Tav[i] = 0;
                }

            }
            return true;
            
        }
        else
            return false;
    }
    
    public double ComputeMeanTav(int SectionID)
    {       
        double sum = 0;
        int count = 0;
        for (Signal sig : this.signals)
        {
            if(!sig.IsDeleted() && sig.IsPeakFound && !sig.IsFiltered())
            {
                if(sig.sections.get(SectionID).IsPeakFound && sig.sections.get(SectionID).IsISILengthBiggerOne()) 
                {
           
                    sum = sum + sig.sections.get(SectionID).getTav();
                    count++;
                }
            }
        }
        if(count>0)
        {
            this.Mean_Tav[SectionID] = sum/count;
            return this.Mean_Tav[SectionID];
        }
        else
            return 0;
    }

    public void ShowSelectedSignalOnSigmaTav(int selectedIndex, int ScetionId)
    {
       int SignalId = this.STD_Tav_Datasets.get(ScetionId).SignalId[selectedIndex];
        this.signalAnalysisForm.ShowSelectedSignalOnSigmaTav(SignalId-1);
    }
    
    public void SaveData(String filePath,Utils.SaveType type, String folderName){
        String delim = "";
        String newLine = "\n";
        String file_extension = "";
        switch(type){
            case txt:{
               delim = "\t";
               file_extension = ".txt";
               break;
            }
            case xls:{
               delim = "\t";
               file_extension = ".xls";
               break;
            }
            case csv:
            {
               delim = ",";
               file_extension = ".csv";
               break;
            }
            case dat:
            {
               delim = ",";
               file_extension = ".dat";
               break;
            }
        }
        SaveAnalysisInfoInCsvFile(filePath,newLine,delim,file_extension,folderName);
        SaveISIsSWs(filePath,newLine,delim,file_extension,folderName);
        SaveSignalsInCSVFileForUser(filePath, newLine,delim,file_extension,folderName);
    }
    
    private void SaveAnalysisInfoInCsvFile(String filePath, String newLine, String delim, String file_extension,String folderName)
    {       
        FileWriter fileWriter = null;
        filePath = filePath + "/" + folderName + "_analysisInfo" + file_extension;

        String File_Header = "Signal_ID"+ delim + "Signal_Name" + delim + "Section_ID" + delim + "Section_Name" + delim + "PeakThreshold" + 
                delim+ "Section_TimeFrom"+ delim + "Section_TimeTo"+ delim+ "Signal_TimeFrom" + delim + "Signal_TimeTo"+ delim + "PeakNumber"+
                delim+ "SectionIntensityMean"+ delim + "SectionIntensitySD"+ delim+ "SectionSNR" +
                delim + "Tav" + delim + "TavSD" +delim + "TavSD/Tav" +delim+ "SpikeWidth" +delim+"SW_SD"+
                delim + "Amplitude" +delim+ "AMP_SD" +delim+ "Enrgy"+
                delim + "Power" + delim + "RootMeanSquare" + delim + "SectionRMSCharge" + delim + "SpikeAreaMean" +delim + "SpikeAreaSD" +
                delim + "SpikeTriangle" + delim +"EffectiveSectionCharge" + 
                delim + "TimeToPeakMean" + delim + "AVCalciumReleasingRate" + delim + "AVCalciumRemovingRate"+
                delim + "PeakValueMean" + delim + "NadirValueMean" + delim + "MeanCalciumMoles" + delim + "MeanCalciumIonsNumber" ;
        
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.append(File_Header);
            fileWriter.append(newLine);
            String s = "";

            for (int i = 0; i < this.NumberOfSections; i++) {
                for (Signal sig : this.signals) {
//                    if (sig.IsPeakComputedForAllSections()) {                       
                        Sections sec = sig.sections.get(i);
                        if(sec!=null && sec.TimeIsAddedToSection && sec.IsPeakFound // && sec.HasMoreThanOnePeak() 
                                && !sig.IsDeleted() && !sig.IsFiltered())
                        {
                            s = String.valueOf(sig.getSig_Id()) + delim + sig.getSig_name() + delim +
                                String.valueOf(sec.getSecID()) + delim + sec.getSectionName() + delim +
                                String.valueOf(sec.getPeakThr()) + delim +                                    
                                String.valueOf(sec.getStartTime()) + delim + String.valueOf(sec.getEndTime()) + delim + 
                                String.valueOf(sig.getStartTime()) + delim + String.valueOf(sig.getEndTime()) + delim +
                                String.valueOf(sec.getPeakNumber()) + delim + String.valueOf(sec.getIntensitiesMean())+ delim +
                                String.valueOf(sec.getIntensitiesSD()) + delim + String.valueOf(sec.getSNR())+ delim +                                
                                String.valueOf(sec.getTav()) + delim +
                                String.valueOf(sec.getSTD())+ delim + String.valueOf(sec.getFreqSNR())+ delim + 
                                String.valueOf(sec.getMeanSW())+ delim +
                                String.valueOf(sec.getSpikeWidthSD())+ delim + String.valueOf(sec.getMeanAMP())+ delim +
                                String.valueOf(sec.getAmplitudeSD())+ delim 
                                +String.valueOf(sec.getEnergy())+ delim + String.valueOf(sec.getPower()) + delim 
                                +String.valueOf(sec.getRootMeanSquareSection()) + delim 
                                +String.valueOf(sec.getRMSCharge()) + delim
                                +String.valueOf(sec.getSpikeAreaAverage())+delim  
                                +String.valueOf(sec.getSpikeAreaSD()) + delim
                                +String.valueOf(sec.getSpikeTriangle()) + delim
                                +String.valueOf(sec.getEffectiveSectionCharge()) + delim
                                +String.valueOf(sec.getTimeToPeakAverage())+ delim 
                                +String.valueOf(sec.getAverageReleasingRate()) + delim + String.valueOf(sec.getAverageRemovingRate())+ delim
                                +String.valueOf(sec.getMeanPeakValue()) + delim + String.valueOf(sec.getMeanNadirValue())+ delim
                                +String.valueOf(sec.getSectionCalciumMoles()) + delim + String.valueOf(sec.getSectionCalciumIonsCount()) + newLine;
                           fileWriter.append(s); 
                        }
                    //}
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
            }
        }       
    }
    
    private void SaveSignalsInCSVFileForUser(String filePath, String newLine, String delim, String file_extension,String folderName) // this function writes all signals
    {       
        FileWriter fileWriter = null;
        String File_Header = "";
        try {
            
            String s = "";
            ArrayList<Signal> sinalsTmp = new ArrayList();
            for (Signal sig : this.signals) {
                
                sinalsTmp.add(sig);
            }
            int count = 0;
            while(!sinalsTmp.isEmpty())
            {
                count++;                
                ArrayList<Signal> sinalsWithSameTime = new ArrayList();
                Signal pivotSig = sinalsTmp.get(0);
                sinalsWithSameTime.add(pivotSig);
                sinalsTmp.remove(0);
                int k= 0;
                while(k<sinalsTmp.size())
                {
                    if(sinalsTmp.get(k).getStartTime() == pivotSig.getStartTime() && sinalsTmp.get(k).getEndTime() == pivotSig.getEndTime())
                    {
                        sinalsWithSameTime.add(sinalsTmp.get(k));
                        sinalsTmp.remove(k);
                    }
                    else
                        k++;
                }
                String newfilePath = filePath + "/data" + String.valueOf(count) + file_extension;
                fileWriter = new FileWriter(newfilePath);                
                File_Header = "Time";
                for(Signal sig : sinalsWithSameTime)
                {
                    File_Header += delim + "Signal-" +String.valueOf(sig.getSig_Id());                  
                }
                fileWriter.append(File_Header);
                fileWriter.append(newLine);
                for(int i=0; i<sinalsWithSameTime.get(0).sig_time.size(); i++)
                {
                    s = String.valueOf(sinalsWithSameTime.get(0).sig_time.get(i));
                    for(Signal sig : sinalsWithSameTime)
                    {
                        s += delim + String.valueOf(sig.sig_value.get(i));
                    }
                    fileWriter.append(s);
                    fileWriter.append(newLine);
                }
                fileWriter.close();
            }
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
            }
        }       
    }
    
    
    
    public void SaveSignalPlotsInPDF(String filePath,int width,int height)throws Exception {
       
        filePath = filePath + "/Signal-Plots.pdf";
        boolean IsWritten = false;
        PDDocument document = new PDDocument();
        for(Signal sig : this.signals)
        {
            if(!sig.IsDeleted() && !sig.IsFiltered())
            {
                SignalPlot signalPlot = sig.createSignalPlot(this);
                if(signalPlot != null)
                {
                    Utils.writeImageIntoFile(filePath, width, height, document, signalPlot.getSinalChart());
                }
                
                PlotPoints ISI_TimePlot = sig.CreateISITimePlot();
                if(ISI_TimePlot!=null)
                {
                    IsWritten = Utils.writeImageIntoFile(filePath, width, height, document, ISI_TimePlot.getChart());
                }
            }            
        }
        document.save(filePath);
        document.close();
        document = null;
               
    }
    
    public void SaveISI_TimePlotsInPDF(String filePath,int width,int height)throws Exception {
       
        filePath = filePath + "/ISI_Time-Plots.pdf";
        boolean IsWritten = false;
        try  (PDDocument document = new PDDocument()) {
            
            for(Signal sig : this.signals)
            {
                if(!sig.IsDeleted() && sig.IsPeakFound && !sig.IsFiltered())
                {
                    PlotPoints ISI_TimePlot = sig.CreateISITimePlot();
                    if(ISI_TimePlot!=null)
                    {
                      IsWritten = Utils.writeImageIntoFile(filePath, width, height, document, ISI_TimePlot.getChart());  
                    }    
                }
            }
            if(IsWritten)
            {
                document.save(filePath);
            }
            document.close();
            
        }
    }
   
    public void SaveSigma_TavPlotsInPDF(String filePath,int width,int height)throws Exception {
       
        filePath = filePath + "/Sigma_Tav-Plot.pdf";
        boolean IsWritten = false;
        try (PDDocument document = new PDDocument()) {
            
            if(this.SigmaTavPlot !=null)
            {
               IsWritten = Utils.writeImageIntoFile(filePath, width, height, document, this.SigmaTavPlot.getChart());

            }  
            if(IsWritten)
            {
                document.save(filePath);
            }
           document.close();
        }
    }
    
    private boolean SaveISIsSWs(String filePath, String newLine, String delim, String file_extension,String folderName)
    {
        FileWriter fileWriter = null;
        String File_Header = "";
        
        boolean IsSaved = false;
        try {            
            String s = "";          
            String newfilePath = filePath + "/" + folderName + "_SigalProfile" + file_extension;
            fileWriter = new FileWriter(newfilePath);                
            File_Header = "signalID" +delim + "sectionID" +delim+ "Peak_No"+ 
                delim+ "Peak_value" + delim + "Nadir_value"+delim+ 
                "SpikeWidth(s)" + delim + "Amplitude" + delim +"ISI_Time(s)" + delim +"ISI(s)" 
                + delim +"SpikeArea" + delim + "SpikeStartTime" + delim + "SpikeEndTime" +
                delim + "SpikeWidthThreshold" + delim + "TimeToPeak" +  delim + "ReleasingRate" + delim + "RemoveRate";
               
                             
            fileWriter.append(File_Header);
            fileWriter.append(newLine);
            for(Signal sig : this.signals)
            {
                if(!sig.IsDeleted() && sig.IsPeakFound && !sig.IsFiltered())
                {
                    for(int j=0; j<sig.NumberOfSections;j++)
                    {
                        Sections sec = sig.sections.get(j);
                        if(sec!= null && !sec.IsFiltered && sec.IsPeakFound)
                        {
                            sec.computeISI();
                            for(int k=0; k<sec.getPeakNumber();k++) //k move over the peaks which are in the current section
                            {
                                //k starts from one, becuase ISIs are computed from the second peak
                                s = String.valueOf(sig.getSig_Id()) + delim + String.valueOf(sec.getSecID())+ delim + String.valueOf(k+1)+
                                    delim + String.valueOf(sec.getMaxPeakValue(k)) + delim + String.valueOf(sec.getMinPeakValue(k))+    
                                    delim + String.valueOf(sec.getSpikeWidth(k)) + delim + String.valueOf(sec.getAmplitude(k))+
                                    delim + String.valueOf(sec.getPeakTimeForOnePeak(k))+ delim + String.valueOf(sec.getISI(k)) +
                                    delim + String.valueOf(sec.getEffectiveSpikeArea(k)) + delim + String.valueOf(sec.getSpikeStartTime(k))+
                                    delim + String.valueOf(sec.getSpikeEndTime(k)) + delim + String.valueOf(sec.getSpikeWidthThrY(k))+
                                    delim + String.valueOf(sec.getTimeToPeak(k))+ delim + String.valueOf(sec.getReleasingRate(k))+
                                    delim + String.valueOf(sec.getRemovingRate(k));

                                fileWriter.append(s);
                                fileWriter.append(newLine);
                                s = "";
                            }
                        }
                    }
                }
            }
            fileWriter.close();
            IsSaved = true;
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                IsSaved = false;
            } catch (IOException e) {
            }
        }
        return IsSaved;
    }
    
    private boolean SaveMinMaxPeaksOfSignals(String filePath, String newLine, String delim, String file_extension)
    {
        FileWriter fileWriter = null;
        String File_Header = "";
       
        boolean IsSaved = false;
        try {            
            String s = "";          
            String newfilePath = filePath + "/MinMaxPeaks" + file_extension;
            fileWriter = new FileWriter(newfilePath);                
            File_Header = "signalID" + delim + "sectionID" + delim+ "PeakMin_time(s)"+delim+ "PeakMin_value" +delim+ "PeakMax_time(s)"+delim+ "PeakMax_value";         
            fileWriter.append(File_Header);
            fileWriter.append(newLine);
            for(Signal sig : this.signals)
            {
                if(!sig.IsDeleted() && sig.IsPeakFound && !sig.IsFiltered())
                {
                    for(int j=0; j<sig.NumberOfSections;j++)
                    {
                        Sections sec = sig.sections.get(j);
                        if(sec!= null && !sec.IsFiltered && sec.IsPeakFound)
                        {
                            for(int k=0; k<sec.getNumberOfMaxPeaks();k++) //k move over the peaks which are in the current section
                            {
                                //k starts from one, becuase ISIs are computed from the second peak
                                s = String.valueOf(sig.getSig_Id()) + delim + String.valueOf(sec.getSecID())+
                                    delim + String.valueOf(sec.getMinPeakTime(k)) + delim + String.valueOf(sec.getMinPeakValue(k))+
                                    delim + String.valueOf(sec.getMaxPeakTime(k))+ delim + String.valueOf(sec.getMaxPeakValue(k)); 

                                fileWriter.append(s);
                                fileWriter.append(newLine);
                                s = "";
                            }
                        }
                    }
                }
            }
            fileWriter.close();
            IsSaved = true;
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                IsSaved = false;
            } catch (IOException e) {
            }
        }
        return IsSaved;
    }
    
    public boolean SaveOriginalSignals(String ExperimentPath) throws IOException
    {
        FileWriter fileWriter = null;
        String File_Header = "";
        String CommaDelim = ",";
        String NewLine = "\n"; 
        boolean SignalIsSaved = false;
        try {            
            String s = "";          
            String newfilePath = ExperimentPath + "/signals.csv";
            fileWriter = new FileWriter(newfilePath);                
            File_Header = "Time";
            for(Signal sig : this.signals)
            {
                File_Header += CommaDelim + "Signal-" +String.valueOf(sig.getSig_Id());                  
            }
            fileWriter.append(File_Header);
            fileWriter.append(NewLine);
            for(int i=0; i<this.Origin_Exp_time.size(); i++)
            {
                s = String.valueOf(this.Origin_Exp_time.get(i));
                for(Signal sig : this.signals)
                {
                    s += CommaDelim + String.valueOf(sig.Origin_sig_value.get(i));
                }
                fileWriter.append(s);
                fileWriter.append(NewLine);
            }
            fileWriter.close();
            SignalIsSaved = true;
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                SignalIsSaved = false;
            } catch (IOException e) {
            }
        }
        return SignalIsSaved;
    }
    
    public boolean SaveExperiment(String ExperimentName) throws IOException
    {
        String MetaFilePath = new File(".").getCanonicalPath() + "/Meta_data";
        boolean IsSaved = false;
        if(Utils.IsCreatedFile(MetaFilePath))
        {
            String ExperimentFile = MetaFilePath + "/" + ExperimentName;
            if(Utils.IsCreatedFile(ExperimentFile))
            {
                IsSaved = this.SaveOriginalSignals(ExperimentFile);
                //IsSaved = IsSaved & this.SaveISIsSWs(ExperimentFile);
                
            }
        }
        
        return IsSaved;
    }

    private Signal sigClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
    


