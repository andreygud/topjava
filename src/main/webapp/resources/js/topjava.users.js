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
});

function flipCheckBox(id) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + id + "/flip"
    }).done(function (data) {
        $(`.checkbox_row[id='${id}']`).attr("data-enabled", data.result);
        $(`.checkbox_row[id='${id}'] .user_checkbox`).prop('checked', data.result);
        successNoty("Checkbox flipped");
    });
};