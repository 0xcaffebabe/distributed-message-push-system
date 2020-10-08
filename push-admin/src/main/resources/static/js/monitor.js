
const connectorTable = document.querySelector('#connectorListTable');
const clientTable = document.querySelector('#clientListTable');

async function getConnectorList(){
    return await axios.get('/api/connector/list')
}

async function getClientList(){
    return await axios.get('/api/client/list')
}

async function kickOut(id) {
    await axios.delete(`/api/client/${id}`)
    alert('踢出客户请求已发送')
}

async function renderConnectorList(){
    const list = await getConnectorList()

    for (let i = 0;i<list.length;i++){
        connectorTable.innerHTML += `
            <tr class="success">
                <td>${list[i].name}</td>
                <td>${list[i].users}</td>
                <td>${list[i].delay}ms</td>
                <td>${list[i].state}</td>
            </tr>
    `
    }
}

async function renderClientList(){
    const list = await getClientList()

    for (let i = 0;i<list.length;i++){
        clientTable.innerHTML += `
            <tr>
                <td>${list[i].id}</td>
                <td>${list[i].connector}</td>
                <td>${list[i].lastActive}</td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="kickOut(${list[i].id})">踢出</button>
                </td>
            </tr>
    `
    }
}

renderConnectorList()
renderClientList()
