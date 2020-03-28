# wave-dsl

A DSL for [segmod](https://github.com/lucdoebereiner/segmod) which allows to easily define frequencies/indexes repetitions.
Live demo: [wave-dsl](http://firmanty.com/wave)
## Syntax
- `[]*N` defines that whole expresion in square brackets should be repeated N times
- `<DURATION FREQ/INDEX>` defines a cell. `DURATION` is one of:
  - `single`/`si` - returns value without repeating it
  - `grain`/`g` - repeats value 1 to 5 times
  - `short`/`s` - repeats value 5 to 44 times
  - `medium`/`m` - repeats value 45 to 244 times
  - `long`/`l` - repeates value 250 to 1249 times
- `FREQ/INDEX` is a float number which will be repeated based on duration
- `#` lines starting with hash sign are comments but no spaces are allowed in comments because I am lazy and wrote that program in the middle of the night. You can use `-` to separate words 
 
## Examples

###Using single
Input
```
#repeat-1-only-once
<si 1>
```
Output
```
1
```

### Repeating sequences
Input
```
#grain-1-and-grain-2-sequence-repeated-twice
[<g 1> <g 2>]*2
```
Output
```
1 1 1 1 1 2 2 2 2 2 1 1 1 1 1 2 2 2 2 2
```

### Nested sequences
Input
```
#nested-sequences-example
[[<si 1> <si 2>]*4 <si 3> <si 4>]*2
```
Output
```
1 2 1 2 1 2 1 2 3 4 1 2 1 2 1 2 1 2 3 4
```
