proj_dir = File.join(File.dirname(__FILE__), '..', '..')
files_dir = File.join(proj_dir,'test','fixtures','files')
debug = false

# 00 00 FE FF UTF-32, big-endian
# FF FE 00 00 UTF-32, little-endian
# FE FF UTF-16, big-endian
# FF FE UTF-16, little-endian
# EF BB BF  UTF-8
#

BOMs = [
  ['UTF-32BE', "\x00\x00\xFE\xFF"],
  ['UTF-32LE', "\xFF\xFE\x00\x00"],
  ['UTF-16BE', "\xFE\xFF"],
  ['UTF-16LE', "\xFF\xFE"],
  ['UTF-8',    "\xEF\xBB\xBF"]
]

sample_input = File.join(files_dir,'sample.in.txt')
from_encoding = 'ASCII'
BOMs.each do |pair|
  to_encoding, bom_bytes = pair
  puts "to_encoding=#{to_encoding} bom_bytes='#{bom_bytes}'" if debug
  output_file = sample_input.sub(/\.in\./, ".#{to_encoding.downcase}.")
  File.open(output_file,"w") do |f|
    f.write bom_bytes
  end

  cmd = "iconv -f #{from_encoding} -t #{to_encoding} #{sample_input} >> #{output_file}"
  puts cmd
  system cmd
end

system "file #{files_dir}/*.txt"

# set -e 
# set -u
# PROJ_DIR=$(dirname $0)/../..
# FILES_DIR=$PROJ_DIR/test/fixtures/files
# 
# if ! which recode; then
#   echo "Error: you must install the recode utiltiy to generate the sample data files (aptitude, brew, yum, etc.)"
#   exit 1
# fi
# 
# cp                     $FILES_DIR/sample.in.txt   $FILES_DIR/sample-ascii.txt
# recode ascii...utf16 < $FILES_DIR/sample.in.txt > $FILES_DIR/sample-utf-16.txt
# recode utf16...utf8  < $FILES_DIR/sample.in.txt > $FILES_DIR/sample-utf-8.txt
