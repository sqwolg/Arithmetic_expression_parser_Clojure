# Arithmetic parser on Clojure


This Clojure script is an arithmetic expression parser that can evaluate mathematical expressions and supports variables.

------------
### Usage

The parser supports the following operations:

- Addition (+)
- Subtraction (-)
- Multiplication (*)
- Division (/)
- Negation (negate)
- Hyperbolic sine (sinh)
- Hyperbolic cosine (cosh)

It also supports parsing expressions with variables. Here's how you can use it:

```clojure
(def expr (parseFunction "(+ x 3)"))
(def vars {'x 2})
(expr vars)  ;; Returns: 5
```

In this example, we first parse the expression "2x - 3" and then evaluate it with x=5, y=2, and z=3.

------------

### Parser Functions
The main parsing function is parseFunction, which takes a string representation of an expression and returns a function that takes a map of variables and evaluates the expression.

The `parseFunctionObject` function is similar, but it returns an object that has `:evaluate` and `:toString` methods instead of a function.

------------

### Creating and Using Operations
You can create new operations by using the create-op function, which takes a symbol and a function as arguments and returns a new operation object. For example:
    
```clojure
(def Square (create-op "square" (fn [f] (* f f))))
```

You can then use this operation in your expressions:
    
```clojure
(def expr (parseFunctionObject "(square x)"))
(def vars {'x 2})
(:evaluate expr vars)  ;; Returns: 4
(:toString expr)  ;; Returns: "(square x)"
```
------------
### Detailed Function Explanation
Here's a more in-depth look at the key functions and how they work:

- `constant`: Creates a constant function that ignores its argument and always returns the given value.
- `variable`: Creates a function that looks up its argument in a map.
- `factory`: Creates a function that applies the given operation to the results of two other functions.
- `negate`, arcTan, arcTan2, add, multiply, subtract, divide: These are specific operations that can be used in expressions.
- `ew-op`: This is a map from operation symbols to their corresponding functions.
- `parses`: This function takes an expression (which can be a number, a symbol, or a list), and returns a function that can evaluate that expression given a map of variables.
- `parseFunction`: This function takes a string, parses it into an expression, and then parses that expression into a function.
- `proto-Var-Const`, proto-Const, proto-Var, Constant, Variable: These are used to create objects that represent variables and constants in expressions.
- `create-op`, Add, Subtract, Multiply, Divide, Negate, Sinh, Cosh: These are used to create objects that represent operations in expressions.
- `ew-op-obj`, parseFunctionObject, parseObject: These are similar to their non-object counterparts, but they work with operation, constant, and variable objects instead of functions.
