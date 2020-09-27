
const connectorTable = document.querySelector('#connectorListTable');
async function getConnectorList(){
    return await axios.get('/api/connector/list')
}

async function renderConnectorList(){
    const list = await getConnectorList()

    for (let i = 0;i<list.length;i++){
        connectorTable.innerHTML += `
            <tr class="success">
                <td>${list[i].name}</td>
                <td>${list[i].delay}ms</td>
                <td>${list[i].state}</td>
            </tr>
    `
    }
}

renderConnectorList()
