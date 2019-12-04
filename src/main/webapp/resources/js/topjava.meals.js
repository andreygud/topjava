// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#meals_datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});


function filter() {
    filterform = $("#filterform")

    $.ajax({
        type: "GET",
        url: context.ajaxUrl + "filter",
        data: filterform.serialize()
    }).done(function (data) {
        refreshTable(data)
        successNoty("Filtered");
    });
}

function clearFilter() {
    $("#filterform").get(0).reset();
    filter();
}

function updateTable() {
    filter()
}