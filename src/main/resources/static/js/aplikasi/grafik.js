google.charts.load('current', {'packages':['line']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    var chartDiv = document.getElementById('chart_div');

    var materialOptions = {
        height: 500,
        width: "100%",
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {
                axis: 'jumlah'
            },
            1: {
                axis: 'nilai'
            }
        },
        axes: {
            // Adds labels to each axis; they don't have to match the axis names.
            y: {
                jumlah: {
                    label: 'Jumlah Transaksi'
                },
                nilai: {
                    label: 'Nilai Transaksi'
                }
            }
        }
    };


    function drawMaterialChart() {
        axios.get('/pembayaran/rekap')
        .then(function (response) {
            console.log(response);

            var dataRekap = [];
            response.data.forEach(function (rekap){
                dataRekap.push(
                    [
                        new Date(rekap.tanggal),
                        rekap.jumlah,
                        rekap.nilai
                    ]
                );
            });

            var data = new google.visualization.DataTable();
            data.addColumn('date', 'Tanggal');
            data.addColumn('number', "Jumlah Pembayaran");
            data.addColumn('number', "Nilai Pembayaran");
            data.addRows(dataRekap);

            var materialChart = new google.charts.Line(chartDiv);
            materialChart.draw(data, materialOptions);
        })
        .catch(function (error) {
            console.log(error);
        });

    }

    drawMaterialChart();

}