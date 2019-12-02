// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );

    $(".user_checkbox").click(function () {
        id = $(this).parents().eq(1).attr('id');
        flipCheckBox(id)
    });

    $(".user_checkbox:checked").parents('tr').css('font-weight', 'bold');

});

function flipCheckBox(id) {
    var url = context.ajaxUrl + id;

    $.get(url, function (data) {
        data.enabled = !data.enabled;

        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8"
        }).done(function () {
            updateTable();
            successNoty("Checkbox flipped");
        });
    });

    $(".user_checkbox:checked").parents('tr').css('font-weight', 'bold');
};