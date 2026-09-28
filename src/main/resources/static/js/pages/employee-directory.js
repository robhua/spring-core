(() => {
	const bulkForm = document.getElementById("bulk-delete-form");
	if (!bulkForm) return;

	const rowCheckboxes = Array.from(document.querySelectorAll(".employee-select"));
	const selectAll = document.getElementById("select-all-employees");
	const deleteButton = document.getElementById("bulk-delete-button");
	const selectedCount = document.getElementById("selected-employee-count");

	const updateSelection = () => {
		const checkedCount = rowCheckboxes.filter((checkbox) => checkbox.checked).length;
		deleteButton.disabled = checkedCount === 0;
		selectedCount.textContent = selectedCount.dataset.countTemplate.replace("{0}", checkedCount);
		selectAll.checked = rowCheckboxes.length > 0 && checkedCount === rowCheckboxes.length;
		selectAll.indeterminate = checkedCount > 0 && checkedCount < rowCheckboxes.length;
	};

	selectAll.addEventListener("change", () => {
		rowCheckboxes.forEach((checkbox) => { checkbox.checked = selectAll.checked; });
		updateSelection();
	});
	rowCheckboxes.forEach((checkbox) => checkbox.addEventListener("change", updateSelection));
	bulkForm.addEventListener("submit", (event) => {
		const checkedCount = rowCheckboxes.filter((checkbox) => checkbox.checked).length;
		if (checkedCount === 0 || !confirm(bulkForm.dataset.confirmMessage)) {
			event.preventDefault();
		}
	});
	document.querySelectorAll(".single-delete-form").forEach((form) => {
		form.addEventListener("submit", (event) => {
			if (!confirm(form.dataset.confirmMessage)) event.preventDefault();
		});
	});
	updateSelection();
})();