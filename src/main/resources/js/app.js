function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}

function switchModal(currentModalID, nextModalID) {
  if (currentModalID) closeModal(currentModalID);
  openModal(nextModalID);
}