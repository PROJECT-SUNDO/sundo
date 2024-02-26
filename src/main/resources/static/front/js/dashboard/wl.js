
const myChart2 = document.getElementById('myChart').getContext('2d');
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