function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    $.ajaxSetup({
        converters: {
            "text json": function (stringData) {
                var json = JSON.parse(stringData);
                $(json).each(function () {
                    if (this.dateTime) {
                        this.dateTime = this.dateTime.replace('T', ' ').substr(0, 16);
                    }
                });
                return json;
            }
        }
    });

    makeEditable({
        ajaxUrl: "ajax/profile/meals/",
        datatableApi: $("#datatable").DataTable({
            "language": {
                "url": `${getLocale()}`
            },
            "ajax": {
                "url": "ajax/profile/meals",
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn

                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });

    initializeMealsDatePickers()
});

function initializeMealsDatePickers() {
    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                maxDate: $('#endDate').val() ? $('#endDate').val() : false,
                minDate: false
            })
        },
        onSelectDate: function () {
            if ($('#startDate').val() === $('#endDate').val()) {
                $("#endTime").datetimepicker('reset');
            }
        }
    });

    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                minDate: $('#startDate').val() ? $('#startDate').val() : false,
                maxDate: false
            })
        },
        onSelectDate: function () {
            if ($('#startDate').val() === $('#endDate').val()) {
                $("#endTime").datetimepicker('reset');
            }
        }
    });

    $("#startTime").datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            if ($('#startDate').val() === $('#endDate').val()) {
                this.setOptions({
                    maxTime: $('#endTime').val() ? $('#endTime').val() : false
                })
            } else {
                this.setOptions({
                    maxTime: false
                })
            }
        }
    });

    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            if ($('#startDate').val() === $('#endDate').val()) {
                this.setOptions({
                    minTime: $('#startTime').val() ? $('#startTime').val() : false
                })
            } else {
                this.setOptions({
                    minTime: false
                })
            }
        }
    });

    $(".datetime-input").datetimepicker({
        format: 'Y-m-d H:i'
    });
}