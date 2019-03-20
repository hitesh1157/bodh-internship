import * as constants from '../config/Constants';
import _ from "lodash";
import ServiceHelper from './helper'


class Quillpad {
	convertWord(word, lang){
		var url = constants.QUILLPAD_FE + word + "?lang=" + lang
    return ServiceHelper.doGetCall(url);
	}

	convertWords(words, lang, callback) {
  	var hindiWords = []
  	var wordsArray = words.trim().split(' ')
  	const hasSpecialChar = (word) => {
  		return (word.indexOf("-") >= 0) ? true : false
  	}
		wordsArray.forEach((word, index, array) => {
	  	if(hasSpecialChar(word)){
	  		hindiWords.push({
	  			index: index,
	  			word: word
	  		})
	  		if (hindiWords.length === wordsArray.length){
	  			var myObjects = _.sortBy(hindiWords, 'index');
	  			var hindiWordsSorted = _.map(myObjects, 'word');
        	callback(hindiWordsSorted.join(' '))
        }
	  	} else {
				this.convertWord(word, lang)
	  		.then((response) => {
	  			hindiWords.push({
          	index: index,
          	word: response['twords'][0]['options'][0]
          })
          if (hindiWords.length === wordsArray.length){
          	var myObjects = _.sortBy(hindiWords, 'index');
		  			var hindiWordsSorted = _.map(myObjects, 'word');
	        	callback(hindiWordsSorted.join(' '))
          }
	  		})
	  		.catch(err => {
	  			alert(err)
	  		})
	  	}
	  })
	}

	//not being used
 //  convertInHindi(words, callback) {
 //  	var url = constants.QUILLPAD
 //  	var hindiWords = []
 //  	var wordsArray = words.split(' ')
 //  	const hasSpecialChar = (word) => {
 //  		return (word.indexOf("-") >= 0) ? true : false
 //  	}

	// 	wordsArray.forEach((word, index, array) => {
	//   	if(hasSpecialChar(word)){
	//   		hindiWords.push({
	//   			index: index,
	//   			word: word
	//   		})
	//   		if (hindiWords.length === wordsArray.length){
	//   			var myObjects = _.sortBy(hindiWords, 'index');
	//   			var hindiWordsSorted = _.map(myObjects, 'word');
 //        	callback(hindiWordsSorted.join(' '))
 //        }
	//   	} else {
	//   		var ddata = {
	//   			lang: 'hindi',
	//   			inString: word,
	//   			scid: 2
	//   		}
	// 	  	$.ajax({
	//         url: url,
	//         data: ddata,
	//         dataType: 'jsonp',
	//         cache: true,
	//         async: false,
	//         success: function (response) {
	//           hindiWords.push({
	//           	index: index,
	//           	word: response['twords'][0]['options'][0]
	//           })
	//           if (hindiWords.length === wordsArray.length){
	//           	var myObjects = _.sortBy(hindiWords, 'index');
	// 		  			var hindiWordsSorted = _.map(myObjects, 'word');
	// 	        	callback(hindiWordsSorted.join(' '))
	//           }
	//         },
	//         error: function (xhr, status) {
	//         	console.log('got error')
	//           alert("error");
	//         }
	//     	});
	//   	}
 //  	})
	// }
}

export default new Quillpad();
