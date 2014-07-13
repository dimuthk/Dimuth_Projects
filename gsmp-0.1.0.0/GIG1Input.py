import clcg4
from estimator import Estimator
import math
from argparse import ArgumentParser
# python shannon.py -n --debug m
# Other arguments and default values are as follows:
# '-n', '--num_reps',  help="Number of simulation repetitions.", type=int, default=1
# '-p', '--arrival_process', help="Estimate performance measure for P", type=int, default=0
# '-t', '--trial', help="Trial run to get required number of replications", action="store_true"
# '-d', '--debug', help="Verbose output needed", type=int, default=0

class Event:
    defined = False
    inf = float("inf")
    def __init__(self):
        if not Event.defined:
            self.event_list = {}
            self.time = 0.0
            Event.defined = True

    def add_event(self, event_name, gen):
        self.event_list[event_name] = {'clock': Event.inf, 'gen': gen}
        
    def next_event(self):
        event_list = self.event_list
        event = min(event_list, key=lambda x: event_list[x]['clock'])
        delta_time = event_list[event]['clock'] - self.time
        self.cancel_clock(event)
        self.time += delta_time
        return event, delta_time

    def set_clock(self, event_name):
        event = self.event_list[event_name]
        if event['clock'] == Event.inf: ## == infinity:
            event['clock'] = event['gen'].next_val() + self.time

    def cancel_clock(self, event_name):
        self.event_list[event_name]['clock'] = Event.inf

    def reset(self):
        self.time = 0.0
        for event in self.event_list:
            self.event_list[event]['clock'] = Event.inf

def triangular(unigen, gen_id, b):
    while True:
        u1 = unigen.next_value(gen_id)
        u2 = unigen.next_value(gen_id)
        yield b * (u1 + u2)
        
def exp(unigen, gen_id, avg_lambda):
    while True:
        u = unigen.next_value(gen_id)
        yield -math.log(u)/avg_lambda

def weibull(unigen, gen_id, avg_lambda, alpha):
    while True:
        u = unigen.next_value(gen_id)
        yield pow(-math.log(u), 1.0/alpha)/avg_lambda

def regen_normal(unigen, gen_id):
    while True:
        u1 = unigen.next_value(gen_id)
        u2 = unigen.next_value(gen_id)
        n1 = math.sqrt(-2 * math.log(u1)) * math.cos(2 * math.pi * u2)
        n2 = math.sqrt(-2 * math.log(u1)) * math.sin(2 * math.pi * u2)
        yield n1
        yield n2

def norm_cdf(x):
    abs_x = abs(float(x))
    a1 = 0.4361836
    a2 = -0.1201676
    a3 = 0.9372980
    y = 1.0 / (1.0 + 0.33267*abs_x)
    phi = 1.0 - (1.0/math.sqrt(2*math.pi)) * (a1*y + a2*pow(y, 2) + a3*pow(y, 3)) \
          * math.exp(-pow(abs_x, 2)/2)
    return phi if x >= 0 else (1 - phi)
    
def exp_corr(unigen, gen_id, c1, c2):
    normal_stream = regen_normal(unigen, gen_id)
    z_prev = next(normal_stream)
    while True:
        z = next(normal_stream)
        yn = c1*z + c2*z_prev
        z_prev = z
        phi_yn = norm_cdf(yn)
        yield -math.log(phi_yn)
    
class RandDist:
    def __init__(self, gen_id, dist_fn, fn_args):
        unigen = clcg4.Clcg4()
        unigen.init_default()
        self.gen = dist_fn(unigen, gen_id, **fn_args)

    def next_val(self):
        return next(self.gen)
        
def transition(cur_state, next_event):
    if next_event is "arrival":
        next_state = cur_state + 1
    elif next_event is "serviced":
        next_state = cur_state - 1
    else:
        raise Exception("Event "+ next_event + " is undefined.")
    return next_state

def do_rep(e):
    queue_tot = 0
    state = 0
    e.reset()
    e.set_clock("arrival")
    while e.time <= 1000:
        next_event, delta_t = e.next_event()

        if e.time + delta_t > 1000:
            delta_t = 1000 - e.time
        queue_tot += state * delta_t
        state = transition(state, next_event)
        if state is not 0:
            e.set_clock("arrival")
            e.set_clock("serviced")
        else:
            e.set_clock("arrival")
    return float(queue_tot)/1000    

P = {0: [0, exp, {'avg_lambda': 1}],
     1: [1, weibull, {'avg_lambda': 0.8856899, 'alpha': 2.1013491}],
     2: [2, weibull, {'avg_lambda': 1.7383757, 'alpha': 0.5426926}],
     3: [3, exp_corr, {'c1': 1.0/math.sqrt(2), 'c2': -1.0/math.sqrt(2)}],
     4: [4, exp_corr, {'c1': 1.0/math.sqrt(2), 'c2': 1.0/math.sqrt(2)}]
     }
     
# parse command line arguments
parser = ArgumentParser(description = "python shannon.py -n --debug m")
parser.add_argument('-n', '--num_reps',
                    help="Number of simulation repetitions.", type=int, default=1)
parser.add_argument('-p', '--arrival_process',
                    help="Estimate performance measure for P", type=int, default=0)
parser.add_argument('-t', '--trial',
                    help="Trial run to get required number of replications", action="store_true")
parser.add_argument('-d', '--debug',
                    help="Verbose output needed", type=int, default=0)
sysargs = parser.parse_args()

# verbose printing for different debug levels
def verbose_print(level, *args):
    if level <= int(sysargs.debug):
        for arg in args:
            print arg,
    else:
        pass

e = Event()
r_arrival = RandDist(*P[sysargs.arrival_process])
r_service = RandDist(5, triangular, {'b': 0.99})
e.add_event("arrival", r_arrival)
e.add_event("serviced", r_service)

est=Estimator(1.96, "95%")
for i in range(sysargs.num_reps):
    q = do_rep(e)

    est.process_next_val(q)
    
print P[sysargs.arrival_process]
print "Point Estimate:", est.get_mean()
print est.get_conf_interval()
print est.get_rel_error()


