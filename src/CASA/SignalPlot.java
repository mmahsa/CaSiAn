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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import static org.jfree.data.statistics.Regression.getPolynomialRegression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public final class SignalPlot{

    private final Shape circle;
    private final Shape dimond;
    private final Shape DiagonalCross;
    private final Shape triangle;    
    public ChartPanel Signalcpanel;
    private XYDataset Signaldataset;
    private JFreeChart Signalchart;
    private final XYLineAndShapeRenderer Signalrenderer;   
    private final double Minx;
    private final double Miny;
    private final double Maxx;
    private final double Maxy;
    private final double extendX;
    private final double extendY;
    private boolean ShowPeaks;    
    private boolean MousIsDragged = false;   
    private javax.swing.JPopupMenu popMenuSignal_Plot;
    private javax.swing.JPopupMenu defaultPopuoMenu;
    Map<String,String> plotIds = new HashMap<String,String>(); 
    private Experiment exp;
    private Signal sig;
    
    public JFreeChart getSinalChart()
    {
        return Signalchart;
    }
    
    public SignalPlot(final Signal sig, final Experiment exp) {
        
        circle = new Ellipse2D.Float(-3.0f, -3.0f, 5.0f, 5.0f);
        DiagonalCross = ShapeUtilities.createDiagonalCross(5, 3.0f);
        dimond = ShapeUtilities.createDiamond(3.0f);
        triangle = ShapeUtilities.createUpTriangle(4.0f);
        this.exp = exp;
        this.sig = sig;
        
        double[] x = new double[sig.sig_time.size()];
        double[] y = new double[sig.sig_time.size()];
        for (int i = 0; i < sig.sig_time.size(); i++) {
            x[i] = sig.sig_time.get(i);
            y[i] = sig.sig_value.get(i);
        }
        Minx = Utils.findMin(x);
        Miny = Utils.findMin(y);
        Maxx = Utils.findMax(x);
        Maxy = Utils.findMax(y);
        
        extendX = (Maxx - Minx)*(0.033);
        extendY = (Maxy - Miny)*(0.07);   
        sig.ZoomedMinX = Minx - extendX;
        sig.ZoomedMaxX = Maxx + extendX;
        sig.ZoomedMinY = Miny - extendY;
        sig.ZoomedMaxY = Maxy + extendY; 
        
        this.ShowPeaks = false;    
        Signaldataset = addDataSet(x, y, "signal");
        x = null;
        y = null;       
        Signalrenderer = new XYLineAndShapeRenderer();
        Signalchart = DrawChart(Signaldataset);
        
        Signalcpanel = new ChartPanel(Signalchart);
        defaultPopuoMenu = Signalcpanel.getPopupMenu();
        createPopupMenu();
        Signalcpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseMotionListener();
        addMouseListener(exp,sig); 
        AddPlotListener();
        plotIds.put("signal",String.valueOf(0));
    }
    
    private void createPopupMenu()
    {
        popMenuSignal_Plot = new JPopupMenu();
        JMenuItem AddPeak = new JMenuItem("Add Peak");
        JMenuItem AddNadir = new JMenuItem("Add Nadir");
        JMenuItem RemoveItem = new JMenuItem("Remove Peak or Nadir");
        popMenuSignal_Plot.add(AddPeak);
        popMenuSignal_Plot.add(AddNadir);
        popMenuSignal_Plot.addSeparator();
        popMenuSignal_Plot.add(RemoveItem);
        AddPeak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                exp.AddSelectedPointAsPeak();
            }
        });
        AddNadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                exp.AddSelectedPointAsNadir();
            }
        });
        RemoveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                exp.RemovePeakorNadir();
            }
        });        
    }
    
    public void AddSectionLines(double[] x1, double[] y1, double[] x2, double[] y2,int SectionID,Signal sig)
    {
        if(this.Signaldataset!=null)
        {
            String ComponentId_1 = "LineMinx" + String.valueOf(SectionID);
            String ComponentId_2 = "LineMaxx" + String.valueOf(SectionID);
            Signaldataset = this.addDataSet(x1, y1, ComponentId_1); 
            Signaldataset = this.addDataSet(x2, y2, ComponentId_2);
            
            plotIds.put(ComponentId_1, String.valueOf(SectionID));
            plotIds.put(ComponentId_2, String.valueOf(SectionID));           
            Signalchart = DrawChart(Signaldataset);            
            Signalcpanel.setChart(Signalchart);
            this.ShowPeaks = false;
            
        }
    }
     
    public void getSignalsWithMaxPeaks(Sections sec,boolean showMinPeaks) {
        
        if (this.Signaldataset != null) {
                        
            String CompenentIdMax = "MaxPeaks" + String.valueOf(sec.getSecID());
            String CompenentIdMin = "MinPeaks" + String.valueOf(sec.getSecID());
                    
            Signaldataset = addDataSet(sec.getMaxPeakTime(),sec.getMaxPeakValues(),CompenentIdMax);
            plotIds.put(CompenentIdMax,String.valueOf(sec.getSecID()));
            
            if(showMinPeaks)
            {
               Signaldataset = addDataSet(sec.getMinPeakTime(),sec.getMinPeakValues(), CompenentIdMin);
               plotIds.put(CompenentIdMin,String.valueOf(sec.getSecID()));
            }
            this.ShowPeaks = true;
            
            Signalchart = DrawChart(Signaldataset);
            
            Signalcpanel.setChart(Signalchart);           
            this.AddPlotListener();
              
        }
    }
   
    public void removePeakPlot(Sections sec)
    {
        if (this.Signaldataset != null) {
            
            //we should remove the datasets with these IDs
            String CompenentIdMax = "MaxPeaks" + String.valueOf(sec.getSecID());
            String CompenentIdMin = "MinPeaks" + String.valueOf(sec.getSecID());
            
            XYSeriesCollection data = ((XYSeriesCollection) this.Signaldataset);
            for (int i = 0; i < data.getSeriesCount(); i++) {
                if (String.valueOf(data.getSeries(i).getKey()).equals(CompenentIdMax))                         
                {
                    data.removeSeries(i);
                }
                if(String.valueOf(data.getSeries(i).getKey()).equals(CompenentIdMin))
                {
                    data.removeSeries(i);
                }
            }
           this.Signaldataset = (XYDataset)data;
           Signalchart = DrawChart(Signaldataset);            
           Signalcpanel.setChart(Signalchart); 
           this.AddPlotListener();
        }
    }
        
    private void addMouseMotionListener()
    {
        Signalcpanel.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            MousIsDragged = true;
            }  
        });      
    }

    public void addMouseListener(final Experiment exp, final Signal sig)
    {
        Signalcpanel.addMouseListener(new MouseListener(){
            
             @Override
              public void mouseReleased(java.awt.event.MouseEvent e){      
                 if (MousIsDragged) {
                     MousIsDragged = false;
                     ChartPanel panel = (ChartPanel) e.getSource();
                     org.jfree.data.Range YR = panel.getChart().getXYPlot().getRangeAxis().getRange();
                     org.jfree.data.Range XR = panel.getChart().getXYPlot().getDomainAxis().getRange();
                     double From = XR.getLowerBound();
                     double To = XR.getUpperBound();
                     boolean outofRange = false;
                     sig.ZoomedMinX = From;
                     sig.ZoomedMaxX = To;
                     sig.ZoomedMinY = YR.getLowerBound();                       
                     sig.ZoomedMaxY = YR.getUpperBound();
                     sig.PlotIsZoomed = true;
                     if (XR.getLowerBound() < Minx) {
                         From = Minx;
                         outofRange = true;
                     }
                     if (XR.getUpperBound() > Maxx) {
                         To = Maxx;
                         outofRange = true;
                     }
                     if (outofRange) {
                         panel.getChart().getXYPlot().getDomainAxis().setRange(Minx - extendX, Maxx + extendX);
                         panel.getChart().getXYPlot().getRangeAxis().setRange(Miny - extendY, Maxy + extendY);
                         sig.ZoomedMinX = Minx - extendX;
                         sig.ZoomedMaxX = Maxx + extendX;
                         sig.ZoomedMinY = Miny - extendY;
                         sig.ZoomedMaxY = Maxy + extendY;
                         
                     }                                      
                    exp.setEditSignalBoxes(From, To,!outofRange); // show numbers in the from and To textBoxes                    
                }
              }
              
              @Override
              public void mouseEntered(java.awt.event.MouseEvent e){                
              }              
              @Override
              public void mouseExited(java.awt.event.MouseEvent e){
                        
              }             
              @Override
              public void mousePressed(java.awt.event.MouseEvent e){
                                   
              }              
              @Override
              public void mouseClicked(java.awt.event.MouseEvent e){
       
              }             
        });
    }      
    public void AddPlotListener() {

       //Signal chart panel has 5 datasets.
        // by clicking on each plot, the index of selected point in that plot is returned.
        Signalcpanel.addChartMouseListener(new ChartMouseListener() {
        @Override
        public void chartMouseClicked(ChartMouseEvent e) {
            ChartEntity entity = e.getEntity();
            if ((entity != null) && (entity instanceof XYItemEntity)) {
                // Get entity details
                //String tooltip = ((XYItemEntity)entity).getToolTipText();
                //XYDataset dataset = ((XYItemEntity) entity).getDataset();
                Signalcpanel.setPopupMenu(popMenuSignal_Plot);
                Signalcpanel.getPopupMenu().show(Signalcpanel, e.getTrigger().getX(), e.getTrigger().getY());
                Signalcpanel.setPopupMenu(defaultPopuoMenu);              
                final int seriesIndex = ((XYItemEntity)entity).getSeriesIndex();
                final XYSeriesCollection XYSeries = ((XYSeriesCollection)((XYItemEntity)entity).getDataset());
                String ComponentId = String.valueOf(XYSeries.getSeries(seriesIndex).getKey());
                int SectionId = Integer.parseInt(plotIds.get(ComponentId));
                exp.setSelectedPointsInLabels(sig, ((XYItemEntity)entity).getItem(), ComponentId, SectionId);                
                final XYPlot xyPlot = (XYPlot) Signalchart.getPlot();                
                xyPlot.setRenderer(new XYLineAndShapeRenderer(){
                    @Override
                    public Shape getItemShape(int seriesIdx, int valueIdx) {
                        String CompId = String.valueOf(XYSeries.getSeries(seriesIdx).getKey());
                        if(CompId.equals(ComponentIDs.signal))
                        {
                            if (valueIdx == sig.getSelectedIndexByPlot()) {
                                return DiagonalCross;

                            } else {

                                return circle;
                            }
                        }
                        else
                            return triangle;
                    }
                    @Override
                    public Paint getItemPaint(int seriesIdx, int valueIdx) {
                        
                        String CompId = String.valueOf(XYSeries.getSeries(seriesIdx).getKey());
                        if(CompId.equals(ComponentIDs.signal))
                        {
                            if (valueIdx == sig.getSelectedIndexByPlot()) {
                                return Color.magenta;
                            } else {
                                return Color.gray;
                            }
                        }                            
                        if(CompId.contains(ComponentIDs.MaxPeaks))
                        {
                           return Color.red; 
                        }
                        if(CompId.contains(ComponentIDs.MinPeaks))
                        {
                           return Color.green;
                        }
                        if(CompId.contains(ComponentIDs.LineMinx))
                        {
                            return Color.pink;
                        }
                        else
                            return Color.orange; //LineMax
                    }
                });
               SetRendererAfterMouseClicked(XYSeries,((XYLineAndShapeRenderer)xyPlot.getRenderer())); 
            }
        }
        @Override
        public void chartMouseMoved(ChartMouseEvent arg0) {
                
        }});        
    }
    
    private void SetRendererAfterMouseClicked(XYSeriesCollection XYSeries, XYLineAndShapeRenderer NewRenderer)
    {       
        for(int i=0; i< XYSeries.getSeriesCount();i++)
        {
            if(String.valueOf(XYSeries.getSeries(i).getKey()).equals(ComponentIDs.signal))
            {
                NewRenderer.setSeriesLinesVisible(i, true);                
                NewRenderer.setSeriesStroke(i, new BasicStroke(2.0f));
            }
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.LineMinx))
            {
                NewRenderer.setSeriesLinesVisible(i, true);
                NewRenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                NewRenderer.setSeriesPaint(i, Color.pink);
                NewRenderer.setSeriesShapesVisible(i, false);
                NewRenderer.setSeriesShapesFilled(i, false);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.LineMaxx))
            {
                NewRenderer.setSeriesLinesVisible(i, true);
                NewRenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                NewRenderer.setSeriesPaint(i, Color.orange);
                NewRenderer.setSeriesShapesVisible(i, false);
                NewRenderer.setSeriesShapesFilled(i, false);
                
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.MaxPeaks))
            {
                NewRenderer.setSeriesLinesVisible(i, false);
                NewRenderer.setSeriesPaint(i, Color.red);
                NewRenderer.setSeriesShape(i, triangle);
                NewRenderer.setSeriesShapesVisible(i, true);
                NewRenderer.setSeriesShapesFilled(i, true);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.MinPeaks))
            {
                NewRenderer.setSeriesLinesVisible(i, false);
                NewRenderer.setSeriesPaint(i, Color.green);
                NewRenderer.setSeriesShape(i, triangle);
                NewRenderer.setSeriesShapesVisible(i, true);
                NewRenderer.setSeriesShapesFilled(i, true);
            }
        }
    }
   
    private XYDataset addDataSet(double[] x, double[] y,String componentId) {
        
        XYSeriesCollection data;
        XYSeries Newseries = new XYSeries(componentId);
        for (int i=0;i<x.length;i++) {
                Newseries.add(x[i],y[i]);
            }
        
        if(this.Signaldataset==null)
        {
           data = new XYSeriesCollection();
           data.addSeries(Newseries);
        }
        else
        {
            data = ((XYSeriesCollection) this.Signaldataset);
            
            XYSeriesCollection XYSeries = new XYSeriesCollection();
            for (int i = 0; i < data.getSeriesCount(); i++) {
                XYSeries.addSeries(data.getSeries(i));
            }
            data.removeAllSeries();
            for (int i = 0; i < XYSeries.getSeriesCount(); i++) {
                if (!String.valueOf(XYSeries.getSeries(i).getKey()).equals(ComponentIDs.signal) && 
                        !String.valueOf(XYSeries.getSeries(i).getKey()).equals(componentId)) {
                    data.addSeries(XYSeries.getSeries(i));
                }
            }
            
            data.addSeries(Newseries);
            XYSeries signal = XYSeries.getSeries(ComponentIDs.signal);
            data.addSeries(signal);
        }
        
        return ((XYDataset)data);
    }
    
    public void AddNewPlotToSignalPlot(String ComponentID,double[] x, double[] y)
    {
        if (this.Signaldataset != null) {            
            String Component_id = ComponentID + String.valueOf(sig.getSig_Id());
            Signaldataset = addDataSet(x,y, Component_id);            
            Signalchart = DrawChart(Signaldataset);            
            Signalcpanel.setChart(Signalchart);                    
        }        
    }
    
    public double[] ComputePolyRegressionParameters(double[] x, double[] y,int curveDegree)
    {
        double[] param = null;
        if (this.Signaldataset != null) {
            XYSeriesCollection dataSet = new XYSeriesCollection();
            XYSeries Newseries = new XYSeries("1");
            for (int i=0;i<x.length;i++) {
                Newseries.add(x[i],y[i]);
            }
            dataSet.addSeries(Newseries);           
            //int fitOrder = curve.ordinal(); // shows degree of ploynomial 
           
            param = getPolynomialRegression(((XYDataset)dataSet),0,curveDegree);  
            Newseries = null;
            dataSet = null;
        }
        return param;
    }
         
    private JFreeChart DrawChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                //"signalId: " + String.valueOf(SignalID), // Signalchart title
                sig.getSig_name(),
                "Time(s)", // x axis label
                "Intensity", // y axis label
                dataset, // dataSeriesCollect
                PlotOrientation.VERTICAL,
                false, // include legend
                false, // tooltips
                false // urls
        );
        Font font = new Font( "Meiryo", Font.ROMAN_BASELINE,15);
        chart.getTitle().setFont(font);
        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();        
        plot.setBackgroundPaint(Color.WHITE);
        if(sig.ZoomedMaxX > sig.ZoomedMinX)
            plot.getDomainAxis().setRange(sig.ZoomedMinX , sig.ZoomedMaxX);
        if(sig.ZoomedMaxY > sig.ZoomedMinY)
            plot.getRangeAxis().setRange(sig.ZoomedMinY , sig.ZoomedMaxY); 
       
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);        
        XYSeriesCollection XYSeries = (XYSeriesCollection)dataset;        
        SetRenderer(XYSeries);           
        plot.setRenderer(Signalrenderer);  
        return chart;
    }
    
    private void SetRenderer(XYSeriesCollection XYSeries)
    {
        for(int i=0; i< XYSeries.getSeriesCount();i++)
        {
            if(String.valueOf(XYSeries.getSeries(i).getKey()).equals(ComponentIDs.signal))
            {
                Signalrenderer.setSeriesLinesVisible(i, true);
                Signalrenderer.setSeriesShape(i, circle);
                Signalrenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                if(!this.ShowPeaks)
                {
                    Signalrenderer.setSeriesPaint(i, Color.darkGray);
                    Signalrenderer.setSeriesShapesVisible(i, false);
                    Signalrenderer.setSeriesShapesFilled(i, false);
                }
                else
                {
                    Signalrenderer.setSeriesPaint(i, Color.gray);
                    Signalrenderer.setSeriesShapesVisible(i, true);
                    Signalrenderer.setSeriesShapesFilled(i, true);
                }
                
            }
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.LineMinx))
            {
                Signalrenderer.setSeriesLinesVisible(i, true);
                Signalrenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                Signalrenderer.setSeriesPaint(i, Color.pink);
                Signalrenderer.setSeriesShapesVisible(i, false);
                Signalrenderer.setSeriesShapesFilled(i, false);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.LineMaxx))
            {
                Signalrenderer.setSeriesLinesVisible(i, true);
                Signalrenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                Signalrenderer.setSeriesPaint(i, Color.orange);
                Signalrenderer.setSeriesShapesVisible(i, false);
                Signalrenderer.setSeriesShapesFilled(i, false);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.MaxPeaks))
            {
                Signalrenderer.setSeriesLinesVisible(i, false);
                Signalrenderer.setSeriesPaint(i, Color.red);
                Signalrenderer.setSeriesShape(i, triangle);
                Signalrenderer.setSeriesShapesVisible(i, true);
                Signalrenderer.setSeriesShapesFilled(i, true);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.MinPeaks))
            {
                Signalrenderer.setSeriesLinesVisible(i, false);
                Signalrenderer.setSeriesPaint(i, Color.green);
                Signalrenderer.setSeriesShape(i, triangle);
                Signalrenderer.setSeriesShapesVisible(i, true);
                Signalrenderer.setSeriesShapesFilled(i, true);
            }
            
            if(String.valueOf(XYSeries.getSeries(i).getKey()).contains(ComponentIDs.SolidLine))
            {
                Signalrenderer.setSeriesLinesVisible(i, true);
                Signalrenderer.setSeriesStroke(i, new BasicStroke(2.0f));
                Signalrenderer.setSeriesPaint(i, Color.GREEN);
                Signalrenderer.setSeriesShapesVisible(i, false);
                Signalrenderer.setSeriesShapesFilled(i, false);
            }
        }
    }

//   private JFreeChart createChart() {
//
//    List<Dot> dots = new ArrayList<Dot>();
//    double xdata1[] = {0.05, 0.20, 0.34, 0.45, 0.5, 0.7, 0.9, 1.0};
//    double ydata1[] = {0.94, 0.80, 0.69, 0.44, 0.31, 0.25, 0.01, 0.0};
//    for ( int i=0; i<xdata1.length; i++ ) {
//      Dot d = new Dot();
//      d.x = xdata1[i];
//      d.y = ydata1[i];
//      dots.add(d);
//    }
//
//    XYDataset dataSeriesCollect = (XYDataset)createData("component", dots);
//    JFreeChart Signalchart = 
//      ChartFactory.createXYLineChart("P-R curve", "Recall", "Precision",
//                                     dataSeriesCollect, PlotOrientation.VERTICAL, 
//                                     true, true, false);
//    XYPlot signalPlot = Signalchart.getXYPlot();
////    StandardXYItemRenderer renderer = (StandardXYItemRenderer) signalPlot.getRenderer();
//    Font font  = new Font( "Meiryo", Font.PLAIN, 12);
//    Font font2 = new Font( "Meiryo", Font.PLAIN, 8);
//    Signalchart.getLegend().setItemFont(font);
//    Signalchart.getTitle().setFont(font);
//    XYPlot xyp = Signalchart.getXYPlot();
//    xyp.getDomainAxis().setLabelFont(font); // X
//    xyp.getRangeAxis().setLabelFont(font); // Y
//    xyp.getDomainAxis().setRange(0,1);
//    xyp.getRangeAxis().setRange(0,1);
//    xyp.getDomainAxis().setTickLabelFont(font2);
//    xyp.getRangeAxis().setTickLabelFont(font2);
//    xyp.getDomainAxis().setVerticalTickLabels(true);
//    xyp.getDomainAxis().setFixedAutoRange(100);
//    xyp.getRangeAxis().setFixedAutoRange(100);
//    
//    // fill and outline
//    XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)signalPlot.getRenderer();
//    r.setSeriesOutlinePaint(0, Color.RED);
//    r.setSeriesOutlinePaint(1, Color.BLUE);
//    r.setSeriesShapesFilled(0, false);
//    r.setSeriesShapesFilled(1, false);
//    
//    return Signalchart;
//  }
    static class Dot {

        double x;
        double y;
    }
    
    public static class ComponentIDs{
        
    final static String signal = "signal";
    final static String MaxPeaks = "MaxPeaks";
    final static String MinPeaks = "MinPeaks";
    final static String LineMinx = "LineMinx";
    final static String LineMaxx = "LineMaxx";
    final static String SolidLine = "SolidLine";
        
    }
}
