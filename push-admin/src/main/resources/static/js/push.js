let isBroadcast = true
const pushTargetSelect = document.querySelector("#pushTarget")
const targetUserInput = document.querySelector("#targetUser")
const pushButton = document.querySelector("#pushButton")
const msgInput = document.querySelector("#msgInput")

pushTargetSelect.addEventListener("change",(e) => {
    onTargetChangeOrClick()
})
pushTargetSelect.addEventListener("click",(e) => {
    onTargetChangeOrClick()
})
pushButton.addEventListener("click", () => {
    const msg = msgInput.value
    const target = targetUserInput.value
    if (isBroadcast) {
        broadcastMessage(msg)
    } else {
        pushMessageToSomeone(msg, target)
    }
})

function onTargetChangeOrClick(){
    isBroadcast = pushTargetSelect.value !== "1"
    if (!isBroadcast) {
        targetUserInput.removeAttribute("disabled")
    }else {
        targetUserInput.setAttribute("disabled",!isBroadcast + "")
    }
}

function broadcastMessage(msg){
    axios.get(`/api/message?msg=${msg}`)
        .then(resp => {
            alert(resp)
        })
}
function pushMessageToSomeone(msg, target){
    axios.get(`/api/message?msg=${msg}&target=${target}`)
        .then(resp => {
            alert(resp)
        })
}