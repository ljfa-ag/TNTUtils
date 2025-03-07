#!/usr/bin/env python3
import argparse, re

parser = argparse.ArgumentParser(description='Generates the code needed in the various places for adding a new config option to TNTUtils')
parser.add_argument('name', help='name of the config option to add, in camelCase')
parser.add_argument('--const_name', help='name prefix of the corresponding comment and default constants, in UPPER_SNAKE_CASE')
parser.add_argument('--type', '-t', default='boolean', help='type of the config option, defaults to boolean')

args = parser.parse_args()

def camel_to_upper_snake(s):
    return re.sub(r'(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])', '_', s).upper()

def uppercase_first(s):
    return s[0].upper() + s[1:]

horiz_line_len = 120

opt_name = args.name
opt_const_name = args.const_name or camel_to_upper_snake(opt_name)
opt_type = args.type

comment_name = opt_const_name + "_COMMENT"
default_name = opt_const_name + "_DEFAULT"

if opt_type == "int":
    opt_boxed_type = "Integer"
else:
    opt_boxed_type = uppercase_first(opt_type)

print("In TNTUtilsConfigAccess:\n" + "-" * horiz_line_len)
print(f"""\
\t{opt_type} {opt_name}();
...
\tstatic final String {comment_name} = "Comment";
\tstatic final {opt_type} {default_name} = false;
""")

fiber_config_type = "ConfigTypes." + opt_boxed_type.upper()

print("In FiberTNTUtilsConfig:\n" + "-" * horiz_line_len)
print(f"""\
\t\tpublic final PropertyMirror<{opt_boxed_type}> {opt_name} = PropertyMirror.create({fiber_config_type});
...
\t\t\t\t\t.beginValue("{opt_name}", {fiber_config_type}, {default_name})
\t\t\t\t\t.withComment({comment_name})
\t\t\t\t\t.finishValue({opt_name}::mirror)
...
\t\t@Override
\t\tpublic {opt_type} {opt_name}() {{
\t\t\treturn {opt_name}.getValue();
\t\t}}
""")

match opt_type:
    case "float":
        neo_value_type = "DoubleValue"
        neo_value_getter = ".get().floatValue()"
    case "boolean" | "int" | "long":
        neo_value_type = uppercase_first(opt_type) + "Value"
        neo_value_getter = ".get()"
    case _:
        neo_value_type = "ConfigValue<" + opt_boxed_type + ">"
        neo_value_getter = ".get()"

print("In NeoforgeTNTUtilsConfig:\n" + "-" * horiz_line_len)
print(f"""\
\t\tpublic final {neo_value_type} {opt_name};
...
\t\t\t{opt_name} = builder
\t\t\t\t\t.comment({comment_name})
\t\t\t\t\t.define("{opt_name}", {default_name});
...
\t\t@Override
\t\tpublic {opt_type} {opt_name}() {{
\t\t\treturn {opt_name}{neo_value_getter};
\t\t}}
""")

print("In Neo en_us.json:\n" + "-" * horiz_line_len)
print(f"""\
\t"tntutils.configuration.{opt_name}": "Translation"
""")
