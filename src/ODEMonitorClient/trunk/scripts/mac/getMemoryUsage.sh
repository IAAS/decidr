#!/bin/bash
# The DecidR Development Team licenses this file to you under
# the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.

# usage getMemoryUsage.sh [precision]
#   default precision: 2

# fail on error
set -e
# uncomment for debugging:
set -x

# variables
decimals=2
[ -n "${1}" ] && decimals=${1}

echo "WARNING: incomplete dummy data"
getTotalMem()
{
  totMem=$(sysctl hw.physmem)
  totMem=$(totMem##* )
  echo $totMem
}

getTotalSwap()
{
  # This is *very* format-dependent - find better way
  swapTot=$(sysctl vm.swapusage)
  lastChar=${swapTot: -1}
  swapTot=${swapTot%%${lastChar}*}
  swapTot=${swapTot##* }
  case ${lastChar} in
    M|m) factor=$((1024*1024));;
    k|K) factor=1024;;
    g|G) factor=$((1024*1024*1024));;
    T|t) factor=$((1024*1024*1024*1024));;
    *) factor=1;;
  esac
  echo $(bc <<< ${swapTot}*${factor})
}

getFreeMem()
{
  freeMem=$(top -l 1 | head -n 4 | grep -i PhysMem)
  freeMem=${freeMem% free*}
  freeMem=$(freeMem##* )
  lastChar=${freeMem: -1}
  freeMem=${freeMem%${lastChar}*}
  case ${lastChar} in
    M|m) factor=$((1024*1024));;
    k|K) factor=1024;;
    g|G) factor=$((1024*1024*1024));;
    T|t) factor=$((1024*1024*1024*1024));;
    *) factor=1;;
  esac
  echo $(bc <<< ${freeMem}*${factor})
}

getFreeSwap()
{
  # This is *very* format-dependent - find better way
  swapFree=$(sysctl vm.swapusage)
  lastChar=${swapTot: -1}
  swapFree=${swapFree%${lastChar}*}
  swapFree=${swapFree##* }
  case ${lastChar} in
    M|m) factor=$((1024*1024));;
    k|K) factor=1024;;
    g|G) factor=$((1024*1024*1024));;
    T|t) factor=$((1024*1024*1024*1024));;
    *) factor=1;;
  esac
  echo $(bc <<< ${swapFree}*${factor})
}

memTotal=$(getTotalMem)
swapTotal=$(getTotalSwap)
memFree=$(getFreeMem)
swapFree=$(getFreeSwap)

memTotal=$(bc <<< ${memTotal}+${swapTotal})
memFree=$(bc <<< ${memFree}+${swapFree})
memUsed=$(bc <<< ${memTotal}-${memFree})

echo $(echo -e "scale=${decimals}\n(${memUsed}/${memTotal})*100" | bc)