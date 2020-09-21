const messageListTable = document.querySelector("#messageListTable")

async function getMessageList(){
    return await axios.get('/api/list')
}

async function renderMessageList(){
    const list = await getMessageList()
    for(let i = 0;i<list.length;i++){
        messageListTable.innerHTML += (`
        <tr>
            <td><div class="messageId" title="${list[i].messageId}">${list[i].messageId}</div></td>
            <td>${list[i].messageTarget === null ? '全体': list[i].messageTarget}</td>
            <th><a href="#"><a href="#">256</a></a></th>
            <td>${list[i].createTime}</td>
            <td><a class="btn btn-primary btn-sm">查看</a></td>
        </tr>
    `)
    }
}

renderMessageList()
