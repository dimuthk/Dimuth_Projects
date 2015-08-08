var hostUri = "http://localhost:8080/PokeServlet"

var cardBackImg = "Card-Back.jpg"

var imgTag = "<img src=\"" + hostUri + "/images/[IMG_NAME]\" height=\"100%\" width=\"100%\" id=[ID]>"


/**
 * Updates the board given the new set of JSON values returned from the server.
 */
function repaintBoard(data) {
	var boardState = data
	
	repaintForPlayer(data.BOARD[0], true)
	repaintForPlayer(data.BOARD[1], false)
	
	$("#p1Active").droppable({
				accept: "*",
				drop: function(event, ui) {
					//ui.draggable.draggable('option', 'revert', false)
					//updateBoard(data)
					handToActive(event, ui, boardState)
				} });
}

function repaintForPlayer(data, p1Orient) {
	if ('PRIZES' in data) {
		var prizes = data.PRIZES
		for (var i = 0; i < prizes.length; i++) {
			var tag = (p1Orient ? "p1Prize" : "p2Prize") + (i+1)
			addImageOfItem(prizes[i], $("#" + tag), false)
		}
	}
	if ('DECK' in data) {
		var deck = data.DECK
		if (deck.length > 0) {
			var tag = p1Orient ? "p1Prize" : "p2Prize"
			addImageOfItem(deck[0], $("#" + tag), false)
			$("#p1DeckDescriptor").html("Cards left: " + deck.length);
		}
	}
	if ('HAND' in data) {
		var hand = data.HAND
		var loc = $(p1Orient ? "#p1HandPanel" : "#p2HandPanel")
		loc.empty()
		for (var i = 0; i < hand.length; i++) {
			var tag = (p1Orient ? "p1Hand" : "p2Hand") + i
			addCardToHand(hand[i], loc, tag, p1Orient)
			$("#" + tag).draggable({revert: true, cursor: 'move'})
		}
	}
	
	if ('ACTIVE' in data) {
		var active = data.ACTIVE
		var tag = p1Orient ? "p1Active" : "p2Active"
		addImageOfItem(active, $("#" + tag), true)
	}
}

function handToActive(event, ui, boardState) {
	$.post(hostUri + "/handToActive", boardState, function(data) {
		updateBoard(data)
	})
}

function isPlaceholder(item) {
	return item.IDENTIFIER == 7
}

function addCardToHand(item, location, idName, showFront) {
	location.append("<div class=\"cardHandDisplay\">"
			+ imgTag.replace("[IMG_NAME]", showFront ? item.IMG_NAME : cardBackImg)
			        .replace("[ID]", idName)
			+ "</div>")
}

function addImageOfItem(item, location, idName, showFront) {
	if (!isPlaceholder(item)) {
		location.html(imgTag.replace("[IMG_NAME]", showFront ? item.IMG_NAME : cardBackImg)
				            .replace("[ID]", idName))
	}
}