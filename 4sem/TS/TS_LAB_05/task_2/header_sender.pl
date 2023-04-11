use HTTP::Daemon;
use HTTP::Status;
use IO::File;  

my $d = HTTP::Daemon->new(LocalAddr => 'localhost', LocalPort => 4321,) || die;
print "Please contact me at: <URL:", $d->url, ">\n";


while (my $c = $d->accept) {
    while (my $r = $c->get_request) {
        if ($r->method eq 'GET') {
            for my $header_field_name ($r->header_field_names) {
                $c->send_response($header_field_name.": ".$r->header($header_field_name));
            }  
        }
        else {
            $c->send_error(RC_FORBIDDEN)
        }
    }
    $c->close;
    undef($c);
}