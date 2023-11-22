grammar RegularGrammar;

regex: branch EOF;

branch: concatenation ('|' concatenation)*;

concatenation: unit+;

unit: atom ('*' | '+' | '?')?;

atom: character
    | group;

group: '(' branch ')';

character: CHAR;

CHAR: [a-zA-Z0-9];
