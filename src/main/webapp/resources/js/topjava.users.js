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
        var id = $(this).parents().closest('tr').attr('id');
        var status = $(this).prop('checked');
        flipCheckBox(id, status)
    });
});

function flipCheckBox(id, status) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + id + "/flip",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        data: $.param({targetState: status})
    }).done(function (data) {
        $(`.checkbox_row[id='${id}']`).attr("data-enabled", data.result);
        $(`.checkbox_row[id='${id}'] .user_checkbox`).prop('checked', data.result);
        successNoty(`User is ${data.result ? 'Enabled' : 'Disabled'}`);
    }).fail(function (jqXHR) {
        $(`.checkbox_row[id='${id}'] .user_checkbox`).prop('checked', !status);
        failNoty(jqXHR);
    });
};