
async function getGatewayStat(){
    return await axios.get('/api/stat/gateway/requests')
}

async function renderGatewayStatPie(){
    const myChart = echarts.init(document.getElementById('gatewayStatPie'));
    const data = await getGatewayStat()
    myChart.setOption({
        series : [
            {
                name: 'Gateway 请求',
                type: 'pie',
                radius: '55%',
                data:[
                    {value:data.success, name:'成功'},
                    {value:data.fail, name:'失败'}
                ]
            }
        ]
    })
}

renderGatewayStatPie()