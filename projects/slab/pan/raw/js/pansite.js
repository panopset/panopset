let scz = "screen";

function toggleScreens() {
    $('#pageToggle').text(scz);
    if (scz === "screen") {
        setPrinter();
    } else {
        setScreen();
    }
}

function updateScreens() {
    vbgd000 = $('#bgdColorInp').val()
    document.documentElement.style.setProperty('--bgd000', vbgd000);
    vfgd000 = $('#fgdColorInp').val()
    document.documentElement.style.setProperty('--fgd000', vfgd000);
}

function resetScreens() {
    setScreen()
}

function setScreen() {
    scz = "screen"
    document.documentElement.style.setProperty('--bgd000', '#000');
    document.documentElement.style.setProperty('--bgd001', '#024');
    document.documentElement.style.setProperty('--bgd002', '#011');
    document.documentElement.style.setProperty('--fgd000', '#cfc');
    document.documentElement.style.setProperty('--fgd001', '#9df');
    document.documentElement.style.setProperty('--fgd002', '#fff');
}

function setPrinter() {
    scz = "printer"
    document.documentElement.style.setProperty('--bgd000', '#fff');
    document.documentElement.style.setProperty('--bgd001', '#fff');
    document.documentElement.style.setProperty('--bgd002', '#fff');
    document.documentElement.style.setProperty('--fgd000', '#000');
    document.documentElement.style.setProperty('--fgd001', '#000');
    document.documentElement.style.setProperty('--fgd002', '#000');
}
