window.addEventListener("DOMContentLoaded", function() {

  /*기본 막대그래프*/
  // 차트를 그럴 영역을 dom요소로 가져온다.
  const myChart1 = document.getElementById('myChart1').getContext('2d');
  const barChart = new Chart(myChart1, {
    // 차트의 종류(String)
    type: 'bar',  // bar, pie, line, doughnut,polarArea
    // 차트의 데이터(Object)
    data: {
        // x축에 들어갈 이름들(Array)
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      // 실제 차트에 표시할 데이터들(Array), dataset객체들을 담고 있다.
      datasets: [{
        // dataset의 이름(String)
        label: '막대차트테스트',
        // dataset값(Array)
        data: [8, 27, 3, 5, 10, 3],
        // dataset의 배경색(rgba값을 String으로 표현)
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)'
        ],
        // dataset의 선 색(rgba값을 String으로 표현)
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)'
        ],
        // dataset의 선 두께(Number)
        borderWidth: 1
      }]
    },
    // 차트의 설정(Object)
    options: {
        // 축에 관한 설정(Object)
      scales: {
        // y축에 대한 설정(Object)
        y: {
            // 시작을 0부터 하게끔 설정(최소값이 0보다 크더라도)(boolean)
            beginAtZero: true
        },
      },
    },
  });

  /*기본 라인그래프*/
  const myChart2 = document.getElementById('myChart2').getContext('2d');
  const lineChart = new Chart(myChart2, {
    type: 'line',  // bar, pie, line, doughnut,polarArea
    data: {
      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
      datasets: [{
        label: '라인차트테스트',
        data: [65, 59, 80, 81, 56, 55, 40],
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
        fill: false
      }]
    },
    options: {
      scales: {
        x: {
          beginAtZero: true
        },
        y: {
          beginAtZero: true
        }
      }
    }
  });

  /*멀티 라인그래프*/
  const myChart3 = document.getElementById('myChart3').getContext('2d');
  const multiLineChart = new Chart(myChart3, {
    type: 'multi_line',  // bar, pie, line, doughnut,polarArea
    data: {
      lables: ['a', 'b', 'c', 'd', 'e'],
      datasets: [{
        label: 'Temperature °C',
        borderColor: 'rgba(255, 99, 132, 1)',
        backgroundColor: 'rgba(255, 99, 132, 1)',
        yAxisID: 'y',
        data: [0, 1, 2, 3, 4, 5],
        borderWidth: 1,
      }, {
        label: 'Pressure (kPa)',
        borderColor: 'rgba(54, 162, 235, 1)',
        backgroundColor: 'rgba(54, 162, 235, 1)',
        yAxisID: 'y1',
        data: [5, 6, 7, 8, 3, 1],
        borderWidth: 1,
      },
      ],
    },

    options: {
      responsive: true,
      interaction: {
        mode: 'index',
        intersect: false,
      },
      stacked: false,
      plugins: {
        title: {
          display: true,
          text: 'Chart.js Line Chart - Multi Axis'
        }
      },
      scales: {
        y: {
          type: 'linear',
          display: true,
          position: 'left',
        },
        y1: {
          type: 'linear',
          display: true,
          position: 'right',

          // grid line settings
          grid: {
            drawOnChartArea: false, // only want the grid lines for one axis to show up
          },
        },
      }
    }
  });

  /*멀티 막대그래프*/
  const myChart4 = document.getElementById('myChart4').getContext('2d');
  const multiBarChart = new Chart(myChart4, {
    type: 'multi_bar',  // bar, pie, line, doughnut,polarArea
    data: {
      labels: ['A', 'B', 'C'],
      datasets: [
        {
          label: 'y axis left',
          yAxisID: 'y-left',
          data: [10, 20, 30],
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)'
          ],
          borderWidth: 1
        },
        {
          label: 'y axis right',
          yAxisID: 'y-right',
          data: [10000000, 5000000, 3000000],
          backgroundColor: [
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
          borderColor: [
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderWidth: 1
        },
      ]
    },

    options: {
      responsive:true,
      // maintainAspectRatio: false,
      scales: {
        x: {
          title: {
            display: true,
            text: 'X Axis Title'
          },
        },
        'y-left': {
          type: 'linear',
          position: 'left',
          title: {
            display: true,
            text: 'Y Axis Left'
          },
          grid: {
            display: false
          },
        },
        'y-right': {
          type: 'linear',
          position: 'right',
          title: {
            display: true,
            text: 'Y Axis Right'
          },
          ticks: {
            callback: (val) => (val.toExponential())
          },
          grid: {
            display: false
          },
        },
      },
    },
  });



});