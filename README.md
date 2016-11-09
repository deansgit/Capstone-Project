# Capstone-Project
This was my final project for Udacity's Android Developer Nanodegree.

Boda - a Korean learning app for beginner to intermediate learners already familiar with reading and pronouncing hangeul. Has flashcards, quizzes, a speedreading challenge, and a profile section that tracks your achievements.

NOTE:
------------------------
Keystore and plain-text passwords in the build.gradle file are dummies. The existing file (in history) will only be used to facilitate Udacity's project review and won't be valid in any published packages. If you are forking, please make your own keystore, put passwords in gradle.properties (which will not be in version control), and then add references to these passwords in the build.gradle file.

This app uses the Revised Romanization scheme for transliterating Hangeul. However, it also uses Unicode mapping for the syllable blocks, which names the blocks in a slightly modified convention from RR. The only time this will be an issue is when viewing the Unicode syllable **names** on the Flashcards and Quizzes.

http://unicode.org/Public/MAPPINGS/VENDORS/MISC/KPS9566.TXT

FlashCard:
------------------------
Shows a Word, it's English translation, and the romanization using the RR scheme. The RR schematic uses digraphs (generally dipthongs) for vowels that don't have a natural counterpart in English. Swiping left or right will show you a new, randomly generated word written in a randomly selected font. Tapping the screen will change the font if you are having a hard time figuring out the characters.

A lot of times, signs in Korea will be written in cursive. To fully benefit from this feature, cycle through the fonts to see what they look like in different stylizations.

Quiz:
------------------------
Multiple choice section that gives users an item and four randomly generated choices in hangeul. For syllables, the item is the official Unicode name for the syllable block, which is generally written as each character in RR. However, Unicode has adopted some slightly modified conventions for voiced consonants and stops. For instance, 굅 would be KIYEOK-OE-PIEUP even though the final consonant in RR would be romanized as BIEUP. A voiced p is named with a PH, e.g. 파 would be written as PHIEUPH-A. 

Since romanization is all about approximating the actual sound, this is not inherently wrong but can be confusing for someone learning with the official romanization style. Updating the names arbitrarily would momentarily make everything more intuitive but would be more difficult to maintain if the mappings were to update in the future. For now, here is a list of characters that are named differently from what we would expect in the RR scheme.

Speed Reader:
------------------------
Flashes a syllable block. Touching the screen prompts user for input. This works best if you have a Korean language keyboard already installed but if you are deeply familiar with the RR scheme, you can also type in the romanized pronunciation of the syllable.

#Transcription rules
(taken from https://en.wikipedia.org/wiki/Revised_Romanization_of_Korean):

Vowel letters
---------------
|Hangeul|Romanization|
|:--:|:--:|
|ㅏ|	a|
|ㅐ|	ae|
|ㅑ|	ya|
|ㅒ|	yae|
|ㅓ|	eo|
|ㅔ|	e|
|ㅕ|	yeo|
|ㅖ|	ye|
|ㅗ|	o|
|ㅘ|	wa|
|ㅙ|	wae|
|ㅚ|	oe|
|ㅛ|	yo|
|ㅜ|	u|
|ㅝ|	wo|
|ㅞ|	we|
|ㅟ|	wi|
|ㅠ|	yu|
|ㅡ|	eu|
|ㅢ|	ui|
|ㅣ|	i|

Consonant letters
-------------------
|Hangul|	RR Initial|	RR Final|
|:--:|:--:|:--:|
|ㄱ|	g|	k|
|ㄲ|	kk|	k|
|ㄴ|	n|	n|
|ㄷ|	d|	t|
|ㄸ|	tt|	–|
|ㄹ|	r|	l|
|ㅁ|	m|	m|
|ㅂ|	b|	p|
|ㅃ|	pp|	–|
|ㅅ|	s|	t|
|ㅆ|	ss|	t|
|ㅇ|	–|	ng|
|ㅈ|	j|	t|
|ㅉ|	jj|	–|
|ㅊ|	ch|	t|
|ㅋ|	k|	k|
|ㅌ|	t|	t|
|ㅍ|	p|	p|
|ㅎ|	h|	h|

