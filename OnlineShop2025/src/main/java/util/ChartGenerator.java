package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Product; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color; 
import java.awt.Font; 
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartGenerator {

    public static Image createCategoryPieChart(List<Product> products, int width, int height) {
        if (products == null || products.isEmpty()) {
            System.out.println("No product data to generate category pie chart.");
            return null; 
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Long> categoryCounts = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        categoryCounts.forEach(dataset::setValue);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Products by Category", 
                dataset,                
                true,                 
                true,                   
                false               
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Электроника", new Color(0x2E, 0x86, 0xC1)); 
        plot.setSectionPaint("Аксессуары", new Color(0xAF, 0x7A, 0xC5));
        plot.setSectionPaint("Книги", new Color(0x48, 0xC9, 0xB0));  
        plot.setSectionPaint("Телефоны", new Color(0xF3, 0x9C, 0x12)); 
        plot.setSectionPaint("Одежда", new Color(0xE7, 0x4C, 0x3C));       

        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
    
        BufferedImage chartImage = pieChart.createBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB, null);
        
        return SwingFXUtils.toFXImage(chartImage, null);
    }
}