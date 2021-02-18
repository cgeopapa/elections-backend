package gr.elections.sad;

import gr.elections.sad.model.Device;
import gr.elections.sad.model.RepresentativeInfo;
import gr.elections.sad.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.digest.DigestUtils;

@SpringBootApplication
@RestController
public class SadApplication
{
    @Autowired
    private DeviceRepository deviceRepository;

    public static void main(String[] args)
    {
        SpringApplication.run(SadApplication.class, args);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String uid)
    {
        String sha1UID = DigestUtils.sha1Hex(uid);

        if(deviceRepository.existsById(sha1UID))
        {
            deviceRepository.deleteById(sha1UID);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Device device = new Device(sha1UID);
        deviceRepository.save(device);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/info")
    @ResponseBody
    public RepresentativeInfo info(@RequestParam String code, @RequestBody String cert)
    {
        //Search db for this cert. If found then
        return new RepresentativeInfo();
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody String pass)
    {
        //Check if this pass is correct with the one in db
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
