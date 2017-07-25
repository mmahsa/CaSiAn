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
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
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
import static org.jfree.data.statistics.Regression.getOLSRegression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author mahsa.moein
 */
public class PlotPoints {
    
    private final Shape circle;
    private final Shape dimond;
    private final Shape DiagonalCross;
    private final Shape triangle;
    private final Shape DiagonalCross2;
    
    public ChartPanel chartPanel;
    private  XYDataset xyDataset;
    private  JFreeChart chartPlot;
    private  XYLineAndShapeRenderer renderer;
    
    private final Color[] c1 = {Color.red,Color.red,Color.blue,Color.blue,Color.cyan,Color.cyan,Color.pink,Color.pink};
    private final Color[] c = {Color.red,Color.blue,Color.cyan,Color.pink};
    private final  Shape[] s;
    private final  Shape[] s1;
    
   
    public JFreeChart getChart()
    {
        return chartPlot;
    }
    
    public PlotPoints()
    {
        circle = new Ellipse2D.Float(-3.0f, -3.0f, 6.0f, 6.0f);
       
        dimond = ShapeUtilities.createDiamond(4.0f);
        
        DiagonalCross = ShapeUtilities.createDiagonalCross(4, 2f);
        
        triangle = ShapeUtilities.createUpTriangle(5.0f);
        
        DiagonalCross2 = ShapeUtilities.createDiagonalCross(3, 1.0f);
       
       s = new Shape[4];
       s[0] = dimond; s[1] = circle; s[2] = triangle; s[3] = DiagonalCross2;
       
       s1 = new Shape[8];
       s1[0] = dimond;  s1[1] = dimond;  s1[2] = circle; 
       s1[3] = circle; s1[4] = triangle; s1[5] = triangle;
       s1[6] = DiagonalCross2; s1[7] = DiagonalCross2;
    }
    
    public void AddPlot(Experiment exp, double[] x, double[] y,String componentId,double MinX,double MinY, 
            double MaxX, double MaxY, String xLabel, String yLabel,boolean AddListener,boolean fitLine )
    {
        xyDataset = this.AddDataSet(x, y, componentId);
        if(fitLine && x.length>1)
        {
            
            xyDataset = this.Regresssion(x, y,componentId,exp);
        }
       
        renderer = new XYLineAndShapeRenderer();
        
        chartPlot = DrawChart(xyDataset, MinX,MinY ,MaxX , MaxY,xLabel,yLabel,componentId);
           
        if(chartPanel== null)
        {
            chartPanel = new ChartPanel(chartPlot);

            if(AddListener && exp!=null)
            {
              AddPlotListener(exp);  
            }
        }
        else
        {
          chartPanel.setChart(chartPlot);
        }
        
    }
    
    private XYDataset AddDataSet(double[] x, double[] y,String componentId) {
                
        XYSeries Newseries = new XYSeries(componentId);
        
        if(x.length < y.length)
        {
        
        for (int i=0;i<x.length;i++) {
                Newseries.add(x[i],y[i]);
            }
        }
        else{
            
            for (int i=0;i<y.length;i++) {
                Newseries.add(x[i],y[i]);
            }
        }

        XYSeriesCollection data;
        if(this.xyDataset==null)
        {
           data = new XYSeriesCollection();
           data.addSeries(Newseries);
        }
        else
        {
            data = ((XYSeriesCollection) this.xyDataset);
            data.addSeries(Newseries);
        }
        
        return ((XYDataset)data);
    }
    
    private JFreeChart DrawChart(final XYDataset dataset, double minX, double minY, double maxX, double maxY, 
            String xLabel, String yLabel,String componentId) {
        
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "", // Signalchart title
                xLabel, // x axis label
                yLabel, // y axis label
                dataset, // dataSeriesCollect
                PlotOrientation.VERTICAL,
                false, // include legend
                false, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = chart.getXYPlot();
        
        plot.setBackgroundPaint(Color.WHITE);
        
//        double extend = 0;
//        if((maxX - minX)!=0)
//         extend= (maxX - minX)*(0.1);
//        else
//            extend = 1;
//        
//        plot.getDomainAxis().setRange(minX - extend , maxX + extend);
//        
//        plot.getRangeAxis().setRange(minY - extend, maxY + extend);
        
        plot.getRangeAxis().setAutoRange(true);
        if(componentId.contains("ISI-Time"))
        {
           plot.getDomainAxis().setRange(minX  , maxX ); 
        }
        
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);   
                
        XYSeriesCollection XYSeries = (XYSeriesCollection)dataset;
         
        SetRenderer(XYSeries,renderer); 
        plot.setRenderer(renderer);
        
        return chart;

    }
    
    private void SetRenderer(XYSeriesCollection XYSeries,XYLineAndShapeRenderer render)
    {
        boolean check = false;
        for(int i=0; i< XYSeries.getSeriesCount();i++)
        {
           String CompId = String.valueOf(XYSeries.getSeries(i).getKey());
           if(CompId.contains("fitLine"))
           {
               check = true;
               break;
           }
        }
        
        if(check)
        {
            for(int i=0; i< XYSeries.getSeriesCount();i++)
            {
                String CompId = String.valueOf(XYSeries.getSeries(i).getKey());

                if(CompId.contains("fitLine"))
                {
                    render.setSeriesLinesVisible(i, true);
                    render.setSeriesPaint(i,c1[i]);
                    render.setSeriesStroke(i, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, 
                            BasicStroke.JOIN_ROUND, 1.0f, new float[] {6.0f, 6.0f}, 0.0f));

                    render.setSeriesShapesVisible(i, false);
                    render.setSeriesShapesFilled(i, false);
                }
                else
                {
                   
                    render.setSeriesLinesVisible(i, false);
                    render.setSeriesPaint(i, c1[i]);
                    render.setSeriesShape(i, s1[i]);
                    render.setSeriesShapesVisible(i, true);
                    render.setSeriesShapesFilled(i, true);

                }
            }
        }
        else
        {
            for(int i=0; i< XYSeries.getSeriesCount();i++)
            {               
                render.setSeriesLinesVisible(i, false);
                render.setSeriesPaint(i, c[i]);
                render.setSeriesShape(i, s[i]);
                render.setSeriesShapesVisible(i, true);
                render.setSeriesShapesFilled(i, true);
                
            }
        }
    }
    
    private void AddPlotListener(final Experiment exp) {
       
        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent e) {
                ChartEntity entity = e.getEntity();

                if ((entity != null) && (entity instanceof XYItemEntity)) {
                    
                    final int seriesIndex = ((XYItemEntity) entity).getSeriesIndex();
                    final int selectedIndex = ((XYItemEntity)entity).getItem();
                    
                    final XYSeriesCollection XYSeries = ((XYSeriesCollection)((XYItemEntity)entity).getDataset());
                    final String ComponentId = String.valueOf(XYSeries.getSeries(seriesIndex).getKey());
                    final XYPlot xyPlot = (XYPlot) chartPlot.getPlot();
                    if(!ComponentId.contains("fitLine"))
                    {
                        String[] st = ComponentId.split("_");
                        final int SectionId = Integer.parseInt(st[0]);
                        exp.ShowSelectedSignalOnSigmaTav(selectedIndex,SectionId);

                        
                        xyPlot.setRenderer(new XYLineAndShapeRenderer() {

                            @Override
                            public Shape getItemShape(int seriesIdx, int valueIdx) {
                                String CompId = String.valueOf(XYSeries.getSeries(seriesIdx).getKey());

                                    if (seriesIdx == seriesIndex && valueIdx == selectedIndex) {                                

                                            return DiagonalCross;
                                    }
                                    else
                                        return s1[seriesIdx];
                            }

                            public Paint getItemPaint(int seriesIdx, int valueIdx) {

                                if (seriesIdx == seriesIndex && valueIdx == selectedIndex) {

                                    return Color.magenta;
                                }
                                else
                                    return c1[seriesIdx];                                                                   
                            }

                        });
                     
                      SetRenderer(XYSeries,((XYLineAndShapeRenderer) xyPlot.getRenderer()));   
                        
                    }
                   
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent arg0) {
                
            }
        });

    }
    
    public XYDataset Regresssion(double[] x,double[] y, String ComponentId, Experiment exp)
    {       
        
        double[][] datapoints = new double[x.length][2];
        
        for(int i=0; i<x.length; i++)
        {
            datapoints[i][0] = x[i];
            datapoints[i][1] = y[i];
        }
                
        double[] param = getOLSRegression(datapoints);
        double slope = param[1];
        double intercept = param[0];       
        
        double[] yLine = new double[x.length];
        double RMSE = 0;
        double sum = 0;
        
        for(int i=0; i<x.length; i++ )
        {
            yLine[i] = slope*x[i] + intercept;
            
            sum = sum + Math.pow((yLine[i] - y[i]),2);
        }
        
        RMSE = Math.sqrt(sum/x.length);
        
        int SectionID = Integer.parseInt(ComponentId.split("_")[0].trim());
        
        if(exp.Sigma_Tav_Slope!=null && exp.Sigma_Tav_Intercept!=null && exp.RMSE!=null)
        {
            exp.Sigma_Tav_Slope[SectionID] = slope;
            exp.Sigma_Tav_Intercept[SectionID] = intercept;
            exp.RMSE[SectionID]= RMSE;
        }
        String CompId = ComponentId + "_fitLine";
        
        XYDataset FitLineDataSet = this.AddDataSet(x, yLine, CompId);
        
        return FitLineDataSet;
        
    }

    
}
