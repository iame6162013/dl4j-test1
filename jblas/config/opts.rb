## --- BEGIN LICENSE BLOCK ---
# Copyright (c) 2009, Mikio L. Braun
# All rights reserved.
# 
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
# 
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
# 
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
# 
#     * Neither the name of the Technische Universität Berlin nor the
#       names of its contributors may be used to endorse or promote
#       products derived from this software without specific prior
#       written permission.
# 
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
# A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
# HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
# LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
# DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
# THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
# (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
## --- END LICENSE BLOCK ---

class Opts
  attr_accessor :opts, :args

  def initialize(args, shortcuts={}, usage=nil)
    @opts = Hash.new
    @args = Array.new
    args.each do |a|
      case a
      when '-h'
        puts usage
        exit
      when '--help'
        puts usage
        exit
      when /\A--/
        a = a[2..-1]
        if a.index('=')
          key, value = a.split('=')
          opts[key.gsub(/-/, '_').to_sym] = value
        else
          opts[a.gsub(/-/, '_').to_sym] = true
        end
      when /\A-/
        key = a[1..1].to_sym
        key = shortcuts[key] if shortcuts.has_key? key
        value = a[2..-1]
        if not value.empty?
          opts[key] = value
        else
          opts[key] = true
        end
      else
        @args << a
      end
    end

    def defined?(v)
      @opts.has_key? v
    end

    def [](key)
      @opts[key]
    end

    def get(key, default)
      if @opts.include? key
        @opts[key]
      else
        default
      end
    end
  end
end
