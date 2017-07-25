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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.jfree.chart.JFreeChart;

public class Utils {

    public final static String xls = "xls";
    //public final static String xlsx = "xlsx";
    public final static String dat = "dat";
    public final static String cvs = "cvs";
    public final static String csv = "csv";
    public final static String txt = "txt";

    public final static String SaveExperiment = "SaveExperiment";
    public final static String SaveFigures = "SaveFigures";

    public final static int NumberOfFloatingPoint = 4;
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getExtension(String fileName) {
        String ext = "";
        String s = fileName;
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static boolean IsDataNumeric(String data) {
        double number = 0;
        try {
            if (!"".equals(data)) {
                number = Double.parseDouble(String.valueOf(data));
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException exc) {

            return false;
        }
    }

    public static String getFlieName_WithoutExt(String fileName) {
        String name = "";
        String s = fileName;
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            name = s.substring(0, i).toLowerCase();
        }
        return name;
    }

    public static boolean CheckFolder(String path) {
        boolean isProperFileFormat = false;

        File file = new File(path);
        if (file.isDirectory()) {
        
            File[] listOfFiles = file.listFiles();

            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        String extension = getExtension(listOfFiles[i]);
                        if (extension != null) {
                            if (extension.equals(xls) || extension.equals(dat)
                                    || extension.equals(txt) || extension.equals(cvs) || extension.equals(csv)) {
                                isProperFileFormat = true;
                                break;
                            }

                        }
                    }

                }
            }
        }
        file = null;
        return isProperFileFormat;
    }

    public static boolean IsFolderEmpty(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        folder = null;
        if (listOfFiles.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static double findMax(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {

            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static double findMin(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {

            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double findMax(List<Double> array) {
        double max = array.get(0);
        for (int i = 1; i < array.size(); i++) {

            if (array.get(i) > max) {
                max = array.get(i);
            }
        }
        return max;
    }

    public static double findMin(List<Double> array) {
        double min = array.get(0);
        for (int i = 1; i < array.size(); i++) {

            if (array.get(i) < min) {
                min = array.get(i);
            }
        }
        return min;
    }

    public static double[] diffArray(double[] array) {
        double[] Diff = new double[array.length - 1];

        for (int i = 0; i < array.length - 1; i++) {
            Diff[i] = array[i + 1] - array[i];
        }

        return Diff;
    }

    public static double getAverage(double[] array) {
        double sum = 0;
        double Average = 0;
        for (int i = 0; i < array.length; i++) {
            sum = sum + array[i];
        }

        Average = (sum / array.length);
        return Average;
    }
    
    public static double getAverage(ArrayList array) {
        double sum = 0;
        double Average = 0;
        if(array!= null && array.size()>0){
            for (int i = 0; i < array.size(); i++) {
                sum = sum + (double)(array.get(i));
            }
            Average = (sum / array.size());
            return Average;
        }
        else
            return 0;
    }

    public static double getSD(double[] array) {
        double Average = getAverage(array);
        double SD = 0;

        for (int i = 0; i < array.length; i++) {
            SD = SD + pow((array[i] - Average), 2);
        }

        SD = sqrt(SD / (array.length-1));

        return SD;
    }
    
    public static double getSD(ArrayList array) {
        double Average = getAverage(array);
        double SD = 0;

        for (int i = 0; i < array.size(); i++) {
            SD = SD + pow(((double)(array.get(i)) - Average), 2);
        }

        SD = sqrt(SD / (array.size()-1));

        return SD;
    }

    public static int findTimeIndexFrom(double timeFrom, ArrayList<Double> timeArray) {

        for (int i = 0; i < timeArray.size(); i++) {
            if (timeArray.get(i) >= timeFrom) {
                return i;
            }

        }

        return -1;
    }

    public static int findTimeIndexTo(double timeTo, ArrayList<Double> timeArray) {

        for (int i = 0; i < timeArray.size(); i++) {
            if (timeArray.get(i) >= timeTo) {
                return i;
            }

        }

        return -1;
    }
    
    public static int findTimeIndex(double time, ArrayList<Double> timeArray) {

        for (int i = 0; i < timeArray.size(); i++) {
            if (timeArray.get(i) == time) {
                return i;
            }

        }

        return -1;
    }

    public static double[] convertDoubleArrayListToArray(ArrayList<Double> arrList) {
        double[] array = new double[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            array[i] = arrList.get(i);
        }
        return array;
    }

    public static int[] convertIntArrayListToArray(ArrayList<Integer> arrList) {
        int[] array = new int[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            array[i] = arrList.get(i);
        }
        return array;
    }

    public static boolean writeImageIntoFile(String filePath, int width, int height, PDDocument document, JFreeChart chartPlot) throws IOException {
        boolean IsWritten = false;
        BufferedImage image;
        PDXObjectImage ximage;
        PDRectangle pageSize;
        PDPage page1;
        PDPageContentStream ContStream;
        try {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            chartPlot.draw(g2, new Rectangle2D.Double(0, 0, width, height));
            g2.dispose();
            ximage = new PDPixelMap(document, image);
            pageSize = new PDRectangle(width + 20, height + 20);
            page1 = new PDPage(pageSize);
            document.addPage(page1);
            ContStream = new PDPageContentStream(document, page1);
            ContStream.drawXObject(ximage, 10, 10, ximage.getWidth(), ximage.getHeight()); 
            ContStream.close();
            IsWritten = true;
        } catch (Exception e) {
            IsWritten = false;
        }
        finally{
            image = null;
            ximage = null;
            pageSize = null;
            page1 = null;
            ContStream = null;
        }
        return IsWritten;
    }
    
    public static void AddScroll(FrmSigAnalysis frame)
    {
        Container con = frame.getContentPane();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();        
        JPanel panel = new JPanel();
        if(height > 800)
        {
            frame.setSize(1110, 830);  
        }
        else
        {
            frame.setSize(1125, 723);
        }
        panel.setBackground(Color.lightGray);
        panel.add(con);       
        JScrollPane scorollbar = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane. HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        frame.setContentPane(scorollbar);         
        frame.pack();
    }
    
    public static ReadFileStatus CheckFileValidity(String File_Path) throws FileNotFoundException, IOException
    {
        BufferedReader br = null;
        ReadFileStatus rfs = ReadFileStatus.FileIsReadCorrectly;
        
        try {
            br = new BufferedReader(new FileReader(File_Path));
            int numberOfColInFirstRow = Utils.NumberOfColumnsInFile(br.readLine());
            int numberOfColInSecondRow = Utils.NumberOfColumnsInFile(br.readLine());
            if(numberOfColInFirstRow != numberOfColInSecondRow)
            {
               rfs = ReadFileStatus.InputFileDoesNotHaveHeader;
            }
        } catch (IOException e) {
            
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
             
            } catch (IOException ex) {
                
            }
        }
        return rfs;
    }

    public static int NumberOfColumnsInFile(String Line)
    {
        int numberofCol = 0;
        if(Line!=null)
        {
//            if (Line.split("\\s+").length > 1) {
//                numberofCol = Line.split("\\s+").length;

//            } else 
            if (Line.split(",").length >1) {
                numberofCol = Line.split(",").length;

            } else if (Line.split("\t").length >1) {
                numberofCol = Line.split("\t").length;
            } 
        }
        return numberofCol;
    }
    
    public static String DoubleToString(double Number)
    {
        String[] Parts = String.valueOf(Number).trim().split(Pattern.quote("."));
        if(Parts!=null && Parts.length>1 && Parts[1].length()>Utils.NumberOfFloatingPoint)
        {
            return String.valueOf(Number).substring(0, Parts[0].length()+Utils.NumberOfFloatingPoint);
        }
        else
        {
            return String.valueOf(Number);
        }
    }
    public static String DoubleToStringEmitFloats(double Number)
    {
        String[] Parts = String.valueOf(Number).trim().split(Pattern.quote("."));
        if(Parts!=null)
        {
            return String.valueOf(Number).substring(0, Parts[0].length());
        }
        else
        {
            return String.valueOf(Number);
        }
    }
     
    public static double Round2Decimal(double Number)
    {
        return Math.round(Number*100.0)/100.0;
    }
    
    public static boolean IsCreatedFile(String AbsolutePath) throws IOException
    {        
        File file = new File(AbsolutePath);
        if(!file.exists())
        {           
            if(file.mkdir())
            {
                return true;
            }
            else
                return false;
        }
        else
            return true;      
    }
    
    public static void FastFourierTransform(double[] x)
    {
//        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
//        Complex[] cmplx= transformer.transform(x, TransformType.FORWARD);
//     
//        double real;    
//        double im;
//        double mag[] = new double[cmplx.length];
//
//        for(int i = 0; i < cmplx.length; i++){
//            real = cmplx[i].getReal();
//            im = cmplx[i].getImaginary();
//            mag[i] = Math.sqrt((real * real) + (im*im)); // equal to abs in matlab
//        }
//        
//        double highetstPower = Utils.findMax(mag);
        
        int Fs = 100;
        double [][] freMag = FFT.getMag(x,Fs);
        double [] freq = new double[freMag.length];
        double[] Mag = new double[freMag.length];
        for(int i=0; i< freMag.length ; i++)
        {
            freq[i] = freMag[i][0];
            Mag[i] = freMag[i][1];
        }
       
       
               
    }
     
    public enum DataSeries {

        signal, MaxPeaks, MinPeaks
    }
    
    public enum frameState {

        FormLoad, OpenSigWithoutTreat, OpenSigSignalWithTreat
    }
    
    public enum ReadFileStatus{
        FileIsReadCorrectly,EmptySiganlArray, InputFileDoesNotHaveHeader, ExceptionInOpening, DelimiterIsNotValid,
        NotEqualNumbersForSignalandTime
    }
    
    public enum SaveType{
        txt,csv,dat,xls
    }
    
    
}
