import sys
import json
import lorun
import shlex
RESULT_STR = [
    'Accepted',
    'Presentation Error',
    'Time Limit Exceeded',
    'Memory Limit Exceeded',
    'Wrong Answer',
    'Runtime Error',
    'Output Limit Exceeded',
    'Compile Error',
    'System Error'
]


def run(cmd, stdIn, stdOut, userOut, timeLimit, memoryLimit):
    result, fileIn, fileOut = None, None, None
    try:
        fileIn = open(stdIn, 'r')
        fileOut = open(userOut, 'w')
        runcfg = {
            'args': shlex.split(cmd),
            'fd_in': fileIn.fileno(),
            'fd_out': fileOut.fileno(),
            'timelimit': timeLimit,
            'memorylimit': memoryLimit,
        }
        result = lorun.run(runcfg)
    except Exception as e:
        result = {'memoryused': 0, 'timeused': 0, 'result': 8,'errormessage': str(e)}
    finally:
        if fileIn is not None:
            fileIn.close()
        if fileOut is not None:
            fileOut.close()
    if result['result'] == 0:
        file1, file2 = None, None
        try:
            file1 = open(userOut, 'r')
            file2 = open(stdOut, 'r')
            rst = lorun.check(file2.fileno(), file1.fileno())
            if rst != 0:
                result = {'memoryused': 0, 'timeused': 0, 'result': rst}
        except Exception as e:
            result = {'memoryused': 0, 'timeused': 0, 'result': 8, 'errormessage': str(e)}
        finally:
            if file1 is not None:
                file1.close()
            if file2 is not None:
                file2.close()
    else:
        result['memoryused'], result['timeused'] = 0, 0
    return result


if __name__ == '__main__':
    param = {
        'cmd': sys.argv[1].replace('@', ' '),
        'tmp': sys.argv[2],
        'timeused': int(sys.argv[3]),
        'memory': int(sys.argv[4]),
        'stdIn': sys.argv[5],
        'stdOut': sys.argv[6]
    }
    res = json.dumps(run(param['cmd'],
                         param['stdIn'],
                         param['stdOut'],
                         param['tmp'],
                         param['timeused'],
                         param['memory']))
    print(res)
