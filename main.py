import subprocess
import sys

stdout, stderr = subprocess.Popen(
	['java', '-Xss50m', '-Xms64m', '-Xmx1024m', '-jar', 'exam.jar'],
	stdout=subprocess.PIPE,
	stderr=subprocess.PIPE
).communicate()

print(stdout.decode("utf-8"))
print(stderr.decode("utf-8") , file=sys.stderr)

