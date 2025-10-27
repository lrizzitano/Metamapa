function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}
function switchModal(nextModalID) {
  const currentID = extractID(this);
  if (currentID) closeModal(this);
  openModal(nextModalID);
}