<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<head>
    <title>Tik-Tak game #${gameId}</title>
</head>
<body>
<table>
    <tr>
        <td id="1"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="2"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="3"><img src="/resources/image/no_image-200x200.jpg"/></td>
    </tr>
    <tr>
        <td id="4"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="5"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="6"><img src="/resources/image/no_image-200x200.jpg"/></td>
    </tr>
    <tr>
        <td id="7"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="8"><img src="/resources/image/no_image-200x200.jpg"/></td>
        <td id="9"><img src="/resources/image/no_image-200x200.jpg"/></td>
    </tr>
</table>
<div class="game">
    Make position
<textarea id="text">
</textarea>
    <button onclick="makeMove()">Send Move</button>
</div>
</body>
</html>
<script type="text/javascript">
    var currentPosition = "---------";

    $(document).ready(function () {
        $("td").on("click", function () {
            this.preventDefault();
            this.val("X");
            this.off('click');
            makeMove(this.id)
        })

    });

    function makeMove(id) {
        var arr = currentPosition.split('');
        arr[id] = 'x';
        currentPosition = arr.join('');

        $.ajax({
            type: 'GET',
            url: '/makeMove',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: {position: currentPosition, sign: 'o', gameId: ${gameId}},
            success: function (data) {
                console.log(data);
                if (data.owin) alert("I win");
                else if (data.xwin) alert("You win");
                else if (data.drawn) alert("Drawn");
                else {
                    var oldPos = currentPosition.split('');
                    var newPos = data.position.split('');
                    var index;
                    for (i = 0; i < 9; i++) {
                        if (oldPos[i] != newPos[i]) index = i;
                    }
                    $("#" + index).val("o").off('click');

//                    $("#text").val(data.position)
                }
            }
        });
    }

</script>