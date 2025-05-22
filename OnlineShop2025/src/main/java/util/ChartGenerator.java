// File: src/util/ChartGenerator.java
package util;

import javafx.embed.swing.SwingFXUtils; // Để chuyển đổi BufferedImage sang JavaFX Image
import javafx.scene.image.Image;
import model.Product; // Hoặc một lớp dữ liệu khác bạn muốn vẽ biểu đồ
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color; // AWT Color
import java.awt.Font; // AWT Font
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartGenerator {

    // Ví dụ: Tạo biểu đồ tròn thể hiện tỷ lệ sản phẩm theo danh mục
    public static Image createCategoryPieChart(List<Product> products, int width, int height) {
        if (products == null || products.isEmpty()) {
            System.out.println("No product data to generate category pie chart.");
            return null; // Hoặc trả về một ảnh placeholder
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Long> categoryCounts = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        categoryCounts.forEach(dataset::setValue);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Products by Category", // Chart title
                dataset,                // Data
                true,                   // Include legend
                true,                   // Tooltips
                false                   // URLs
        );

        // Tùy chỉnh biểu đồ (tùy chọn)
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Электроника", new Color(0x2E, 0x86, 0xC1)); // Blue
        plot.setSectionPaint("Аксессуары", new Color(0xAF, 0x7A, 0xC5)); // Purple
        plot.setSectionPaint("Книги", new Color(0x48, 0xC9, 0xB0));    // Teal
        plot.setSectionPaint("Телефоны", new Color(0xF3, 0x9C, 0x12)); // Orange
        plot.setSectionPaint("Одежда", new Color(0xE7, 0x4C, 0x3C));    // Red
        // Thêm màu cho các danh mục khác nếu cần

        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        // plot.setBackgroundPaint(null); // Nền trong suốt cho plot area
        // pieChart.setBackgroundPaint(null); // Nền trong suốt cho toàn bộ chart

        // Render biểu đồ thành BufferedImage
        BufferedImage chartImage = pieChart.createBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB, null);
        // BufferedImage chartImage = pieChart.createBufferedImage(width, height, null); // Cách khác


        // Chuyển BufferedImage thành JavaFX Image
        return SwingFXUtils.toFXImage(chartImage, null);
    }

    // Bạn có thể thêm các phương thức tạo biểu đồ khác ở đây (bar chart, line chart, ...)
    // Ví dụ: biểu đồ doanh thu theo tháng, top sản phẩm bán chạy, ...
}