var context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({
        cache: false,
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

function add() {
    $("#modalTitle").html(i18n["addTitle"]);
    form.find(":input").val("");
    $("#editRow").modal();
}

function updateRow(id) {
    $("#modalTitle").html(i18n["editTitle"]);
    $.get(context.ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}

function deleteRow(id) {
    if (confirm(i18n['common.confirm'])) {
        $.ajax({
            url: context.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            context.updateTable();
            successNoty("common.deleted");
        });
    }
}

function updateTableByData(data) {
    context.datatableApi.clear().rows.add(data).draw();
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        context.updateTable();
        successNoty("common.saved");
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(key) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + i18n[key],
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + i18n["common.errorStatus"] + ": " + jqXHR.status + (jqXHR.responseJSON ? "<br>" + jqXHR.responseJSON : ""),
        type: "error",
        layout: "bottomRight"
    }).show();
}

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}

function getLocale() {
    var lang = navigator.language;
    if (supportedLang.includes(lang)) {
        return 'resources/i18n/' + lang + '.json'
    } else {
        return 'resources/i18n/en-US.json'
    }
}