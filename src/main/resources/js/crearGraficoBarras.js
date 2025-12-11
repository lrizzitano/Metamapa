function crearGraficoBarras(datos) {

    am5.ready(function () {

        var root = am5.Root.new("chartdiv");
        root.setThemes([am5themes_Animated.new(root)]);

        var chart = root.container.children.push(
            am5xy.XYChart.new(root, {})
        );

        // eje X
        var xAxis = chart.xAxes.push(
            am5xy.CategoryAxis.new(root, {
                categoryField: datos.ejeX,
                renderer: am5xy.AxisRendererX.new(root, {})
            })
        );

        // eje Y
        var yAxis = chart.yAxes.push(
            am5xy.ValueAxis.new(root, {
                renderer: am5xy.AxisRendererY.new(root, {})
            })
        );

        // vinculamos los datos del eje x
        xAxis.data.setAll(datos.datos);

        // generar dinámicamente las columnas
        datos.columnas.forEach(col => {

            var serie = chart.series.push(
                am5xy.ColumnSeries.new(root, {
                    name: col.nombre,
                    xAxis: xAxis,
                    yAxis: yAxis,
                    valueYField: col.campo,
                    categoryXField: datos.ejeX
                })
            );

            serie.data.setAll(datos.datos);
            serie.appear(1000);
        });

        chart.appear(1000, 100);

        window.addEventListener("unload", () => root.dispose());
    });
}